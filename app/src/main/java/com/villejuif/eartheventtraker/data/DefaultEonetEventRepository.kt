package com.villejuif.eartheventtraker.data

import androidx.lifecycle.LiveData
import com.villejuif.eartheventtraker.EonetRepository
import com.villejuif.eartheventtraker.network.EonetEvent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

class DefaultEonetEventRepository(private val eonetEventRemoteSource : EonetEventsSource,
                                                      private val eonetEventLocalSource : EonetEventsSource) : EonetRepository{

    override fun observeEvents(): LiveData<Result<List<EonetEvent>>> = eonetEventLocalSource.observeEvents()

    override suspend fun getEonetEvents(forceUpdate: Boolean):Result<List<EonetEvent>>{
        if(forceUpdate){
            try {
                updateEonetEventsFromRemoteSource()
            }catch (e: Exception){
                return Result.Error(e)
            }
        }
        return eonetEventLocalSource.getEonetEvents()
    }

    override suspend fun refreshEvents() = updateEonetEventsFromRemoteSource()

    private suspend fun updateEonetEventsFromRemoteSource(){
        val remoteEonetEvents = eonetEventRemoteSource.getEonetEvents()

        if(remoteEonetEvents is Result.Success){

            eonetEventLocalSource.deleteAllEonetEvents()
            remoteEonetEvents.data.forEach { event ->

                if (event.id.isEmpty() or event.id.isBlank()) return
                if (event.categories.isNullOrEmpty()) return
                if (event.geometries.isNullOrEmpty()) return

                eonetEventLocalSource.saveEonetEvent(event)

            }
        }else if(remoteEonetEvents is Result.Error){
            throw remoteEonetEvents.exception
        }
    }

    override suspend fun deleteEonetEvent(eventID:String){
        coroutineScope{
            launch { eonetEventLocalSource.deleteEonetEvent(eventID) }
        }
    }

    override suspend fun getEonetEventWithId(eventID:String):Result<EonetEvent>{
        return eonetEventLocalSource.getEonetEvent(eventID)
    }

}