package com.villejuif.eartheventtraker.maps

import android.util.Log
import androidx.lifecycle.*
import com.villejuif.eartheventtraker.EonetRepository
import com.villejuif.eartheventtraker.data.Result
import com.villejuif.eartheventtraker.events.EarthEventsStatus
import com.villejuif.eartheventtraker.network.EonetEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class MapFilter(val value: String) {
    REQUESTED("Requested"),
    ALL("All"),
    WILDFIRES("Wildfires"),
    STORMS("Storms"),
    ICE("Sea Ice"),
    VOLCANO("Volcano")
}

class MapsViewModel(private val defaultRepository: EonetRepository) : ViewModel() {

    private val _forceUpdate = MutableLiveData<Boolean>()

    private val _requestedItem = MutableLiveData<EonetEvent>()

    val requestedItem: LiveData<EonetEvent> = _requestedItem

    private val _items: LiveData<List<EonetEvent>> =
        Transformations.switchMap(_forceUpdate) { forceUpdate ->
            if (forceUpdate) {
                viewModelScope.launch {
                    try {
                        defaultRepository.refreshEvents()
                    } catch (e: Exception) {
                        Log.e("EarthEventsViewModel", e.message!!)
                    }
                }
            }

            defaultRepository.observeEvents().switchMap { transform(it) }

        }

    val items: LiveData<List<EonetEvent>> = _items

    private val _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean> = _loadingStatus

    private val _mapPOI = MutableLiveData<List<EonetEvent>>()
    val mapPOI: LiveData<List<EonetEvent>> = _mapPOI

    val mapFilterChipNames: List<String> = MapFilter.values().map { it.value }

    private var filter = FilterHolder()

    private var currentFilterJob: Job? = null

    fun requestEvent(id: String) {

        _loadingStatus.value = true

        viewModelScope.launch {
            try {
                val result = defaultRepository.getEonetEventWithId(id)

                if (result is Result.Success) {
                    _requestedItem.value = result.data
                }
            } catch (e: Exception) {
                Log.e("MapsViewModel", e.message!!)
            }

            _forceUpdate.value = false
        }
    }

    private fun transform(events: Result<List<EonetEvent>>): LiveData<List<EonetEvent>> {
        val result = MutableLiveData<List<EonetEvent>>()

        if (events is Result.Success) {
            viewModelScope.launch {
                result.value = events.data
            }
        } else {
            result.value = emptyList()
        }

        return result
    }

    fun changeFilter(filter: String, isChecked: Boolean) {
        if (this.filter.update(filter, isChecked)) {
            onQueryChanged()
        }
    }

    private fun onQueryChanged() {
        onLoading()
        currentFilterJob?.cancel()
        currentFilterJob = viewModelScope.launch {
            when (filter.currentValue) {
                MapFilter.REQUESTED.value -> {
                    _requestedItem.value?.let { _mapPOI.value = listOf(it) }
                }
                MapFilter.ALL.value -> {
                    _items.value?.let { _mapPOI.value = it }
                }
                MapFilter.WILDFIRES.value -> filterByCategory(MapFilter.WILDFIRES.toString())
                MapFilter.STORMS.value -> filterByCategory(MapFilter.STORMS.toString())
                MapFilter.ICE.value -> filterByCategory(MapFilter.ICE.toString())
                MapFilter.VOLCANO.value -> filterByCategory(MapFilter.VOLCANO.toString())
                else -> {
                    stopLoading()
                    return@launch
                }
            }

        }
    }

    private fun filterByCategory(category: String) {
        if (_items.value.isNullOrEmpty()){
            stopLoading()
            return
        }

        val filteredEvents = _items.value!!.asSequence()
            .filter {
                var sameCategory = false
                it.categories?.forEach { it1 ->
                    sameCategory = sameCategory.or(it1.title.contains(category, ignoreCase = true))
                }
                return@filter sameCategory
            }.toList()

        if (!filteredEvents.isNullOrEmpty())
            _mapPOI.value = filteredEvents
        else stopLoading()
    }

    fun currentFilter() = filter.currentValue

    private fun onLoading() {
        _loadingStatus.value = true
    }

    fun stopLoading() {
        _loadingStatus.value = false
    }

    private class FilterHolder {
        var currentValue: String? = null
            private set

        fun update(changedFilter: String, isChecked: Boolean): Boolean {
            if (isChecked) {
                currentValue = changedFilter
                return true
            } else if (currentValue == changedFilter) {
                currentValue = null
                return true
            }
            return false
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MapsViewModelFactory(
    private val defaultRepository: EonetRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (MapsViewModel(defaultRepository) as T)
}