package com.jtw.appetizing

import android.app.Application
import com.jtw.appetizing.dagger.DaggerApplicationComponent

class AppetizingApplication : Application() {

    val daggerComponent = DaggerApplicationComponent
            .builder()
            .application(this)
            .build()
}