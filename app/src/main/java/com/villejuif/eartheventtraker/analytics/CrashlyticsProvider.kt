package com.villejuif.eartheventtraker.analytics

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsProvider {

    private val mFirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    fun nonFatalCrash(throwable: Throwable) {
        mFirebaseCrashlytics.recordException(throwable)
    }
}