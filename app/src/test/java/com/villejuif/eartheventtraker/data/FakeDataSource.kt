package com.villejuif.eartheventtraker.data

import androidx.lifecycle.LiveData
import com.villejuif.eartheventtraker.network.EonetEvent
import java.lang.Exception

class FakeDataSource(var events: MutableList<EonetEvent>? = mutableListOf()) : EonetEventsSource{
    override suspend fun getEonetEvents(): Result<List<EonetEvent>?> {
        events?.let { return Result.Success(it) }

        return Result.Error(Exception("Events not found"))
    }

    override fun observeEvents(): LiveData<Result<List<EonetEvent>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteEonetEvent(eventID: String) {
    }

    override suspend fun deleteAllEonetEvents() {
    }

    override suspend fun saveEonetEvent(event: EonetEvent) {
    }

    override suspend fun getEonetEvent(eventID: String): Result<EonetEvent> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}