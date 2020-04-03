package com.villejuif.eartheventtraker.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EonetEvent(val id : String?,
                      val title : String?,
                      val categories : List<EonetCategory>?,
                      val sources : List<EonetSource>?,
                      val geometries : List<EonetGeometry>?):Parcelable


@Parcelize
data class EonetCategory(val id : String?, val title: String?):Parcelable
@Parcelize
data class EonetSource(val id: String?, val url: String?):Parcelable
@Parcelize
data class EonetGeometry(val date:String?, val type : String?, val coordinates : List<Float>?):Parcelable
