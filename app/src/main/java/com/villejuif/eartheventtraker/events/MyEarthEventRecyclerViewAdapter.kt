package com.villejuif.eartheventtraker.events

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.villejuif.eartheventtraker.analytics.AnalyticsProvider
import com.villejuif.eartheventtraker.databinding.EarthEventItemBinding


import com.villejuif.eartheventtraker.network.EonetEvent



class MyEarthEventRecyclerViewAdapter(
    private val viewModel: EarthEventsViewModel
) : ListAdapter<EonetEvent, MyEarthEventRecyclerViewAdapter.ViewHolder>(EonetEventDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bin(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: EarthEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bin(viewModel: EarthEventsViewModel, item : EonetEvent){
            binding.viewmodel = viewModel
            binding.event = item
            binding.executePendingBindings()
            AnalyticsProvider.reportEarthEvent(item.id, item.categories?.get(0)?.title ?: return,
                item.geometries[0].date, item.geometries[0].coordinates)
        }

        companion object{
            fun from(parent: ViewGroup):ViewHolder{
                val layoutInflater =  LayoutInflater.from(parent.context)
                val binding = EarthEventItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

}

class EonetEventDiffCallback : DiffUtil.ItemCallback<EonetEvent>() {
    override fun areItemsTheSame(oldItem: EonetEvent, newItem: EonetEvent): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EonetEvent, newItem: EonetEvent): Boolean {
        return oldItem == newItem
    }
}
