package com.villejuif.eartheventtraker.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NasaEonetModel(val events:List<EonetEvent>?): Parcelable