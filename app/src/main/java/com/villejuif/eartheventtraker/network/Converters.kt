package com.villejuif.eartheventtraker.network

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {

    @TypeConverter
    fun listCoordFrom(value: String): List<Float> {
        val listType = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun stringFromListCoord(value: List<Float>): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun listCatFrom(value: String): List<EonetCategory> {
        val listType = object : TypeToken<ArrayList<EonetCategory?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun stringFromListCat(value: List<EonetCategory>): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun listSourcesFrom(value: String): List<EonetSource>{
        val listType = object : TypeToken<ArrayList<EonetSource?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun stringFromListSources(value: List<EonetSource>):String{
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun listGeomsFrom(value: String): List<EonetGeometry>{
        val listType = object : TypeToken<ArrayList<EonetGeometry?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun stringFromListGeoms(value: List<EonetGeometry>):String{
        val gson = Gson()
        return gson.toJson(value)
    }

}