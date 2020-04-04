package com.villejuif.eartheventtraker.data.local

import com.villejuif.eartheventtraker.data.EonetEventsSource
import com.villejuif.eartheventtraker.data.Result
import com.villejuif.eartheventtraker.network.EonetEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class EonetEventsLocalSource internal constructor(
    private val eonetEventDao: EonetEventDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): EonetEventsSource {


    override suspend fun getEonetEvents(): Result<List<EonetEvent>?> {
        return Result.Success(eonetEventDao.getEvents())
    }

    override suspend fun deleteEonetEvent(eventID : String) = withContext<Unit>(ioDispatcher){
        eonetEventDao.deleteEventkById(eventID)
    }

    override suspend fun deleteAllEonetEvents() = withContext(ioDispatcher){
        eonetEventDao.deleteEvents()
    }

    override suspend fun saveEonetEvent(event: EonetEvent) {
        eonetEventDao.insertEvent(event)
    }

    override suspend fun getEonetEvent(eventID: String): Result<EonetEvent> = withContext(ioDispatcher){
        try {
            val event = eonetEventDao.getEventById(eventID)
            if(event != null){
                return@withContext Result.Success(event)
            }else{
                return@withContext Result.Error(Exception("Event not found!"))
            }
        }catch (e: Exception){
            return@withContext Result.Error(e)
        }
    }
}