package com.villejuif.eartheventtraker

import androidx.lifecycle.LiveData
import com.villejuif.eartheventtraker.data.EonetEventsSource
import com.villejuif.eartheventtraker.data.Result
import com.villejuif.eartheventtraker.network.EonetEvent
import java.lang.Exception

object FakeFailingEonetEventsRemoteSource : EonetEventsSource {
    override suspend fun getEonetEvents(): Result<List<EonetEvent>> {
        return Result.Error(Exception("Test"))
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