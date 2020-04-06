package com.villejuif.eartheventtraker.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EonetSource(val id: String = "",
                       val url: String = ""): Parcelable