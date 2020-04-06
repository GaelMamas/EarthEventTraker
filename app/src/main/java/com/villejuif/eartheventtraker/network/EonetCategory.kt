package com.villejuif.eartheventtraker.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EonetCategory( val id : String = "",
                          val title: String = ""): Parcelable