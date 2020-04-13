package com.villejuif.eartheventtraker

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.villejuif.eartheventtraker.databinding.FragmentMapsBinding

class MapsFragment: Fragment(), OnMapReadyCallback {

    private val TAG = MapsFragment::class.java.simpleName
    private lateinit var mMap: GoogleMap

    private lateinit var viewDataBinding: FragmentMapsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        @Suppress("UNUSED_VARIABLE")
        viewDataBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_maps, container, false)

        return viewDataBinding.root
    }

    override fun onActivityCreated(p0: Bundle?) {
        super.onActivityCreated(p0)

        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        setupNavigation()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        setMapStyle(googleMap)
    }

    private fun setMapStyle(map: GoogleMap){
        try{
            val success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))

            if(!success) Log.e(TAG, "Style parsing failed.")

        }catch (e: Resources.NotFoundException){
            Log.e(TAG, "Can't find style. Error: " + e.localizedMessage)
        }
    }



    private fun setupNavigation(){

        val onBackPressedCallback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_mapsFragment_to_earthEventsFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

    }
}