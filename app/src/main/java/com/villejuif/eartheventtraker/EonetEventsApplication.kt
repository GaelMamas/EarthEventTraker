package com.villejuif.eartheventtraker

import android.app.Application
import com.villejuif.eartheventtraker.analytics.AnalyticsProvider
import com.villejuif.eartheventtraker.data.DefaultEonetEventRepository

class EonetEventsApplication: Application() {

    val defaultRepository: EonetRepository
    get() = ServiceLocator.provideEonetEventsRepository(this)

    override fun onCreate() {
        super.onCreate()
        AnalyticsProvider.instance(applicationContext)
    }

}