package com.villejuif.eartheventtraker.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.villejuif.eartheventtraker.data.Result
import com.villejuif.eartheventtraker.network.EonetEvent

@Dao
interface EonetEventDao {

    @Query("SELECT * FROM EonetEvents")
    fun observeEvents(): LiveData<List<EonetEvent>>

    @Query("SELECT * FROM EonetEvents WHERE entryid = :eventId")
    fun observeEventById(eventId: String): LiveData<EonetEvent>

    @Query("SELECT * FROM EonetEvents")
    suspend fun getEvents(): List<EonetEvent>

    @Query("SELECT * FROM EonetEvents WHERE entryid = :eventId")
    suspend fun getEventById(eventId: String): EonetEvent?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EonetEvent)

    @Update
    suspend fun updateTask(task: EonetEvent): Int

    @Query("DELETE FROM EonetEvents WHERE entryid = :eventId")
    suspend fun deleteEventkById(eventId: String): Int

    @Query("DELETE FROM EonetEvents")
    suspend fun deleteEvents()
}
