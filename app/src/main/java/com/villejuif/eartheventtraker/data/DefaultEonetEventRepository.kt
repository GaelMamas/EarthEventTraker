package com.villejuif.eartheventtraker.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.villejuif.eartheventtraker.data.local.EonetEventDatabase
import com.villejuif.eartheventtraker.data.local.EonetEventsLocalSource
import com.villejuif.eartheventtraker.data.remote.EonetEventsRemoteSource
import com.villejuif.eartheventtraker.network.EonetEvent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

class DefaultEonetEventRepository private constructor(application: Application){

    private val eonetEventRemoteSource : EonetEventsSource
    private val eonetEventLocalSource : EonetEventsSource

    companion object{
        @Volatile
        private var INSTANCE: DefaultEonetEventRepository? = null

        fun getRepository(app : Application):DefaultEonetEventRepository{
            return INSTANCE ?:synchronized(this){
                DefaultEonetEventRepository(app).also { INSTANCE = it }
            }
        }
    }

    init {
        val database = Room.databaseBuilder(application.applicationContext,
            EonetEventDatabase::class.java, "EonetEvents.db")
            .build()

        eonetEventRemoteSource = EonetEventsRemoteSource
        eonetEventLocalSource = EonetEventsLocalSource(database.eonetEventDao())
    }

    fun observeEvents(): LiveData<Result<List<EonetEvent>>> = eonetEventLocalSource.observeEvents()

    suspend fun getEonetEvents(forceUpdate: Boolean = false):Result<List<EonetEvent>?>{
        if(forceUpdate){
            try {
                updateEonetEventsFromRemoteSource()
            }catch (e: Exception){
                return Result.Error(e)
            }
        }
        return eonetEventLocalSource.getEonetEvents()
    }

    suspend fun refreshEvents() = updateEonetEventsFromRemoteSource()

    private suspend fun updateEonetEventsFromRemoteSource(){
        val remoteEonetEvents = eonetEventRemoteSource.getEonetEvents()

        if(remoteEonetEvents is Result.Success){

            eonetEventLocalSource.deleteAllEonetEvents()
            remoteEonetEvents.data?.forEach { event ->

                if (event.id.isEmpty() or event.id.isBlank()) return
                if (event.categories.isNullOrEmpty()) return
                if (event.geometries.isNullOrEmpty()) return

                eonetEventLocalSource.saveEonetEvent(event)

            }
        }else if(remoteEonetEvents is Result.Error){
            throw remoteEonetEvents.exception
        }
    }

    suspend fun deleteEonetEvent(eventID:String){
        coroutineScope{
            launch { eonetEventLocalSource.deleteEonetEvent(eventID) }
        }
    }

    suspend fun getEonetEventWithId(eventID:String):Result<EonetEvent>{
        return eonetEventLocalSource.getEonetEvent(eventID)
    }

}