package com.villejuif.eartheventtraker.events

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.villejuif.eartheventtraker.data.DefaultEonetEventRepository
import com.villejuif.eartheventtraker.data.Result
import com.villejuif.eartheventtraker.network.EonetEvent
import kotlinx.coroutines.launch

enum class EarthEventsStatus {LOADING, ERROR, DONE}

class EarthEventsViewModel(application: Application):AndroidViewModel(application) {


    private val defaultRepository = DefaultEonetEventRepository.getRepository(application)

    private val _forceUpdate = MutableLiveData<Boolean>()

    private val _items: LiveData<List<EonetEvent>> = Transformations.switchMap(_forceUpdate){forceUpdate ->
        if(forceUpdate){
            _status.value = EarthEventsStatus.LOADING
            _dataLoading.value = true
            viewModelScope.launch{
                try {
                    defaultRepository.refreshEvents()
                    _status.value = EarthEventsStatus.DONE
                }catch (e: Exception){
                    Log.e("EarthEventsViewModel", e.message!!)
                    _status.value = EarthEventsStatus.ERROR
                }
                _dataLoading.value = false
            }
        }

        defaultRepository.observeEvents().switchMap { transform(it) }

    }

    val items: LiveData<List<EonetEvent>> = _items

    private val isDataLoadingError = MutableLiveData<Boolean>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _status = MutableLiveData<EarthEventsStatus>()
    val status: LiveData<EarthEventsStatus> = _status

    init {
        loadEvents(true)
    }

    private fun loadEvents(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    private fun transform(events: Result<List<EonetEvent>>): LiveData<List<EonetEvent>> {
        val result = MutableLiveData<List<EonetEvent>>()

        if(events is Result.Success){
            isDataLoadingError.value = false
            viewModelScope.launch { result.value = events.data}
        }else{
            result.value = emptyList()
            isDataLoadingError.value = true
        }

        return result
    }

    fun refresh(){
        _forceUpdate.value = true
    }


}