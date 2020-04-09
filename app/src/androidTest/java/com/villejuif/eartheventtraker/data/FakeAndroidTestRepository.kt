package com.villejuif.eartheventtraker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.villejuif.eartheventtraker.EonetRepository
import com.villejuif.eartheventtraker.network.EonetEvent
import kotlinx.coroutines.runBlocking

class FakeAndroidTestRepository: EonetRepository {

    var eonetServiceData: LinkedHashMap<String, EonetEvent> = LinkedHashMap()


    private val observableEvents = MutableLiveData<Result<List<EonetEvent>>>()


    override fun observeEvents(): LiveData<Result<List<EonetEvent>>> {
        runBlocking { refreshEvents() }
        return observableEvents
    }

    override suspend fun getEonetEvents(forceUpdate: Boolean): Result<List<EonetEvent>> {
        return Result.Success(eonetServiceData.values.toList())
    }

    override suspend fun refreshEvents() {
        observableEvents.value = getEonetEvents()
    }

    override suspend fun deleteEonetEvent(eventID: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getEonetEventWithId(eventID: String): Result<EonetEvent> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun addEvents(vararg events: EonetEvent){
        for (event in events){
            eonetServiceData[event.id] = event
        }
        runBlocking { refreshEvents() }
    }

}