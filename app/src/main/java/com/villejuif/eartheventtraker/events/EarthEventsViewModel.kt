package com.villejuif.eartheventtraker.events

import android.util.Log
import androidx.lifecycle.*
import com.villejuif.eartheventtraker.EonetRepository
import com.villejuif.eartheventtraker.data.Result
import com.villejuif.eartheventtraker.network.EonetEvent
import kotlinx.coroutines.launch

enum class EarthEventsStatus {LOADING, ERROR, DONE}

class EarthEventsViewModel(private val defaultRepository: EonetRepository):ViewModel() {

    private val _forceUpdate = MutableLiveData<Boolean>()

    private val _items: LiveData<List<EonetEvent>> = Transformations.switchMap(_forceUpdate){forceUpdate ->
        if(forceUpdate){
            _status.value = EarthEventsStatus.LOADING
            _dataLoading.value = true
            viewModelScope.launch{
                try {
                    defaultRepository.refreshEvents()
                }catch (e: Exception){
                    Log.e("EarthEventsViewModel", e.message!!)
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

    private val _clickedEvent = MutableLiveData<EonetEvent>()
    val clickedEvent: LiveData<EonetEvent> = _clickedEvent

    init {
        loadEvents()
    }

    private fun loadEvents(forceUpdate: Boolean = true) {
        _status.value = EarthEventsStatus.DONE
        _forceUpdate.value = forceUpdate
    }

    private fun transform(events: Result<List<EonetEvent>>): LiveData<List<EonetEvent>> {
        val result = MutableLiveData<List<EonetEvent>>()

        if(events is Result.Success){
            isDataLoadingError.value = false
            viewModelScope.launch {
                result.value = events.data
                _status.value = EarthEventsStatus.DONE
            }
        }else{
            result.value = emptyList()
            isDataLoadingError.value = true
            _status.value = EarthEventsStatus.ERROR
        }

        return result
    }

    fun refresh(){
        _forceUpdate.value = true
    }

    fun onClickEvent(event: EonetEvent){
        _clickedEvent.value = event
    }


}

@Suppress("UNCHECKED_CAST")
class EarthEventsViewModelFactory (
    private val defaultRepository: EonetRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (EarthEventsViewModel(defaultRepository) as T)
}