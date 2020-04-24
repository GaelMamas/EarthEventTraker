package com.villejuif.eartheventtraker.analytics

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.analytics.FirebaseAnalytics

object AnalyticsProvider {

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    fun instance(context: Context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    private fun bundle(
        para1Title: String?, para1: String,
        para2Title: String? = null, para2: String? = null,
        para3Title: String? = null, para3: String? = null,
        para4Title: String? = null, para4: String? = null
    ): Bundle {
        return Bundle().apply {
            para1Title?.let { it1 -> putString(it1, para1) }
            para2Title?.let { it2 -> putString(it2, para2) }
            para3Title?.let { it3 -> putString(it3, para3) }
            para4Title?.let { it4 -> putString(it4, para4) }
        }
    }

    fun mapChip(chipName: String) {
        mFirebaseAnalytics.logEvent("MaterialChip", bundle("chip", chipName))
    }

    fun mapPOI(categoryTitle: String, source: String) {
        mFirebaseAnalytics.logEvent(
            "EarthEventPOI",
            bundle(
                "categoryTitle", categoryTitle,
                "source", source
            )
        )
    }

    fun reportWeirdness(kind: String, value: String) {
        mFirebaseAnalytics.logEvent("Weirdness", bundle(kind, value))
    }

    fun reportEarthEvent(id: String, category: String, date: String, coordinates: List<Float>?) {
        mFirebaseAnalytics.logEvent(
            "EarthEvent", bundle(
                "EonetID", id, "Category", category,
                "EonetDate", date, "Coordinates", TextUtils.join(",", coordinates ?: return)
            )
        )
    }


}