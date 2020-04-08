package com.villejuif.eartheventtraker.data

import androidx.lifecycle.LiveData
import com.villejuif.eartheventtraker.network.EonetEvent

interface EonetEventsSource {
    suspend fun getEonetEvents(): Result<List<EonetEvent>>

    fun observeEvents(): LiveData<Result<List<EonetEvent>>>

    suspend fun deleteEonetEvent(eventID : String)

    suspend fun deleteAllEonetEvents()

    suspend fun saveEonetEvent(event: EonetEvent)

    suspend fun getEonetEvent(eventID: String): Result<EonetEvent>
}