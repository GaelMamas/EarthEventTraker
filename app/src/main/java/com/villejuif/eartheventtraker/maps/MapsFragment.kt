package com.villejuif.eartheventtraker.maps

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.villejuif.eartheventtraker.EonetEventsApplication
import com.villejuif.eartheventtraker.R
import com.villejuif.eartheventtraker.databinding.FragmentMapsBinding
import com.villejuif.eartheventtraker.events.src
import com.villejuif.eartheventtraker.network.EonetEvent


class MapsFragment : Fragment(), OnMapReadyCallback {

    private val TAG = MapsFragment::class.java.simpleName

    private val viewModel: MapsViewModel by viewModels {
        MapsViewModelFactory((requireContext().applicationContext as EonetEventsApplication).defaultRepository)
    }

    private lateinit var mMap: GoogleMap
    private lateinit var viewDataBinding: FragmentMapsBinding
    private lateinit var chipGroup: ChipGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        @Suppress("UNUSED_VARIABLE")
        viewDataBinding = FragmentMapsBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }

        return viewDataBinding.root
    }

    override fun onActivityCreated(p0: Bundle?) {
        super.onActivityCreated(p0)

        viewDataBinding.lifecycleOwner = viewLifecycleOwner

        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        setupNavigation()

        arguments?.getString("eonetEventID")?.let {
            viewModel.requestEvent(it)
        }

        setupChips()

        observeAllItems()
    }

    private fun setupNavigation() {

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_mapsFragment_to_earthEventsFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    private fun setupChips() {
        viewModel.requestedItem.observe(viewLifecycleOwner, object : Observer<EonetEvent> {
            override fun onChanged(event: EonetEvent?) {
                event ?: return
                chipGroup = viewDataBinding.filterList
                val inflater = LayoutInflater.from(chipGroup.context)
                val children = viewModel.mapFilterChipNames.map { filter ->
                    val chip = inflater.inflate(R.layout.chip_filterable, chipGroup, false) as Chip
                    chip.text = filter
                    chip.tag = filter

                    chip.setOnCheckedChangeListener { button, isChecked ->

                        viewModel.currentFilter()?.let {
                            if ( button.text.contains(it)) return@setOnCheckedChangeListener
                        }

                        if (!MapFilter.REQUESTED.value.contains(button.text, ignoreCase = true)) {
                            val requestedChip = chipGroup.children
                                .filter {
                                    MapFilter.REQUESTED.value.contains(
                                        (it as Chip).text,
                                        ignoreCase = true
                                    )
                                }.toList()[0] as Chip

                            requestedChip.isChecked = false
                        }

                        viewModel.changeFilter(button.tag as String, isChecked)

                    }

                    if (viewModel.currentFilter().isNullOrEmpty())
                        chip.isChecked =
                            MapFilter.REQUESTED.value.contains(filter, ignoreCase = true)


                    chip
                }

                chipGroup.removeAllViews()

                for (chip in children) {
                    chipGroup.addView(chip)
                }
            }
        })
    }

    private fun observeAllItems() {
        viewModel.items.observe(viewLifecycleOwner, Observer { })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setUpEvents(googleMap)
        setMapStyle(googleMap)
        setInfoWindowClick(googleMap)
    }

    private fun setUpEvents(map: GoogleMap) {
        viewModel.mapPOI.observe(viewLifecycleOwner, Observer { pois ->
            if (pois.isNullOrEmpty()){
                viewModel.stopLoading()
                return@Observer
            }
            map.clear()
            pois.asSequence().forEach {
                it.geometries.forEach { it0 ->
                    it0.coordinates?.let { it1 ->
                        setupEventMarkers(map,
                            it.title, if (it.sources.isNotEmpty()) it.sources[0].url else null,
                            if (it1.size > 1) it1[1].toDouble() else return@let,
                            it1[0].toDouble(), it.categories?.let { it2 -> src(it2) }
                        )
                    }
                }
            }

            viewModel.stopLoading()

        })
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            val success =
                map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))

            if (!success) Log.e(TAG, "Style parsing failed.")

        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: " + e.localizedMessage)
        }
    }

    private fun setInfoWindowClick(map: GoogleMap) {
        map.setOnInfoWindowClickListener {
            it.snippet?.let { url ->
                val browserIntent = Intent(Intent.ACTION_VIEW)
                browserIntent.data = Uri.parse(url)
                activity?.startActivity(browserIntent)
            }
        }
    }

    private fun setupEventMarkers(
        map: GoogleMap,
        title: String, source: String?,
        lat: Double,
        lng: Double,
        @DrawableRes icon: Int? = null
    ) {
        val location = LatLng(lat, lng)

        val option = MarkerOptions()
            .position(location)
            .title(title)
            .snippet(source)

        icon?.let {
            val circleDrawable = resources.getDrawable(it, context?.theme)
            option.icon(getMarkerIconFromDrawable(circleDrawable))
        }

        map.addMarker(option)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 3f))
    }

    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor? {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}