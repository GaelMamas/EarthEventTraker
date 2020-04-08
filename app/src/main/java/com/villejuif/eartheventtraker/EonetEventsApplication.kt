package com.villejuif.eartheventtraker

import android.app.Application
import com.villejuif.eartheventtraker.data.DefaultEonetEventRepository

class EonetEventsApplication: Application() {

    val defaultRepository: EonetRepository
    get() = ServiceLocator.provideEonetEventsRepository(this)

}