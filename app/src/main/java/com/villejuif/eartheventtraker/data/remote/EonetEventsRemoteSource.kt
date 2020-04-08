package com.villejuif.eartheventtraker.data.remote

import androidx.lifecycle.LiveData
import com.villejuif.eartheventtraker.data.EonetEventsSource
import com.villejuif.eartheventtraker.data.Result
import com.villejuif.eartheventtraker.network.EonetEvent
import com.villejuif.eartheventtraker.network.NasaEonetApi

object EonetEventsRemoteSource :
    EonetEventsSource {
    override suspend fun getEonetEvents(): Result<List<EonetEvent>?> {
        var result:Result<List<EonetEvent>?> = Result.Loading
        result = try {
            Result.Success(NasaEonetApi.retrofitService.getEarthEvents().await().events)
        }catch (e:Exception){
            Result.Error(e)
        }

        return result
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