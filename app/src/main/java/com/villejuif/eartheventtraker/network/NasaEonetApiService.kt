package com.villejuif.eartheventtraker.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.villejuif.eartheventtraker.data.Result
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap

enum class NasaEonetFilter(val value:String){
    STATUS_OPEN("open"),
    STATUS_CLOSED("closed"),
    SOURCE_INCI("inciWeb"),
    SOURCE_EO("EO"),
    LIMIT("limit"),
    DAYS("days")
}

private const val BASE_URL = "https://eonet.sci.gsfc.nasa.gov/api/v2.1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface NasaEonetApiService{
@GET("events")
fun getEarthEvents(@QueryMap map:Map<String, String> = mapOf( NasaEonetFilter.LIMIT.value to "30",
    NasaEonetFilter.DAYS.value to "50", "status" to NasaEonetFilter.STATUS_OPEN.value))
        : Deferred<NasaEonetModel>
}

object NasaEonetApi{
    val retrofitService : NasaEonetApiService by lazy { retrofit.create(NasaEonetApiService::class.java) }
}