package com.villejuif.eartheventtraker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.villejuif.eartheventtraker.network.Converters
import com.villejuif.eartheventtraker.network.EonetEvent

@Database(entities = [EonetEvent::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class EonetEventDatabase : RoomDatabase() {
    abstract  fun eonetEventDao(): EonetEventDao
}