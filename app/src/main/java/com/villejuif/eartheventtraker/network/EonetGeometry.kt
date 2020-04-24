package com.villejuif.eartheventtraker.network

import android.os.Parcelable
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EonetGeometry( val date:String = "",
                          val type : String = "",
                          @TypeConverters(Converters::class)
                          val coordinates : List<Float>? = null): Parcelable