package com.villejuif.eartheventtraker

import androidx.lifecycle.LiveData
import com.villejuif.eartheventtraker.data.Result
import com.villejuif.eartheventtraker.network.EonetEvent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

interface EonetRepository {

    fun observeEvents(): LiveData<Result<List<EonetEvent>>>

    suspend fun getEonetEvents(forceUpdate: Boolean = false): Result<List<EonetEvent>>

    suspend fun refreshEvents()

    suspend fun deleteEonetEvent(eventID:String)

    suspend fun getEonetEventWithId(eventID:String): Result<EonetEvent>

}