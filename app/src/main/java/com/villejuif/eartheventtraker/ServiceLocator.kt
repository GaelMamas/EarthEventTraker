package com.villejuif.eartheventtraker

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.villejuif.eartheventtraker.data.DefaultEonetEventRepository
import com.villejuif.eartheventtraker.data.EonetEventsSource
import com.villejuif.eartheventtraker.data.local.EonetEventDatabase
import com.villejuif.eartheventtraker.data.local.EonetEventsLocalSource
import com.villejuif.eartheventtraker.data.remote.EonetEventsRemoteSource
import com.villejuif.eartheventtraker.network.EonetSource
import kotlinx.coroutines.runBlocking

object ServiceLocator {
    private val lock = Any()
    private var database: EonetEventDatabase? = null

    @Volatile
    var eonetRepository: EonetRepository? = null
    @VisibleForTesting set

    fun provideEonetEventsRepository(context: Context): EonetRepository{
        synchronized(this){
            return eonetRepository ?: createEonetRepository(context)
        }
    }

    private fun createEonetRepository(context: Context): EonetRepository{
        val newRepo = DefaultEonetEventRepository(EonetEventsRemoteSource,
            createEonetLocalDataSource(context))
        eonetRepository = newRepo
        return newRepo
    }

    private fun createEonetLocalDataSource(context: Context) : EonetEventsSource{
        val database = database ?: createDataBase(context)
        return EonetEventsLocalSource(database.eonetEventDao())
    }

    private fun createDataBase(context: Context): EonetEventDatabase{
        val result = Room.databaseBuilder(context,
            EonetEventDatabase::class.java, "EonetEvents.db"
        ).build()
        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository(){
        synchronized(lock){
            runBlocking { EonetEventsRemoteSource.deleteAllEonetEvents() }
        }

        database?.apply { clearAllTables()
        close()}

        database = null
        eonetRepository = null
    }

}