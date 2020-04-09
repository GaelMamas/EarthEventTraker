package com.villejuif.eartheventtraker.events

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.villejuif.eartheventtraker.EonetEventsApplication
import com.villejuif.eartheventtraker.R
import com.villejuif.eartheventtraker.databinding.FragmentEarthEventsListBinding


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [EarthEventsFragment.OnListFragmentInteractionListener] interface.
 */
class EarthEventsFragment : Fragment() {

    private val viewModel: EarthEventsViewModel by viewModels(){
        EarthEventsViewModelFactory((requireContext().applicationContext as EonetEventsApplication).defaultRepository)
    }

    private lateinit var viewDataBinding: FragmentEarthEventsListBinding

    private lateinit var listAdapter: MyEarthEventRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentEarthEventsListBinding.inflate(inflater, container, false).apply { viewmodel = viewModel }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()

        setupSwipeRefreshLayout()

    }

    private fun setupSwipeRefreshLayout() {
        viewDataBinding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
            ContextCompat.getColor(requireActivity(), R.color.colorAccent),
            ContextCompat.getColor(requireActivity(), R.color.colorPrimaryDark)
        )
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = MyEarthEventRecyclerViewAdapter(viewModel)
            viewDataBinding.eventsList.adapter = listAdapter
        } else {
            Log.d("EarthEventsFragment","ViewModel not initialized when attempting to set up adapter.")
        }
    }
}
