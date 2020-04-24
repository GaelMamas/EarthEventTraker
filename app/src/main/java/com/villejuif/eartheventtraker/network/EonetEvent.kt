package com.villejuif.eartheventtraker.network

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "eonetEvents")
data class EonetEvent @JvmOverloads constructor(
    @PrimaryKey @ColumnInfo(name = "entryid") val id: String = "",
    @ColumnInfo(name = "title") val title: String = "",
    @TypeConverters(Converters::class)
    val categories: List<EonetCategory>? = null,
    @TypeConverters(Converters::class)
    val sources: List<EonetSource> = emptyList(),
    @TypeConverters(Converters::class)
    val geometries: List<EonetGeometry> = emptyList()):Parcelable
