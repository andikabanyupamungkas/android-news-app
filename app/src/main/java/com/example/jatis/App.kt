package com.example.jatis

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : MultiDexApplication() {

    companion object {
        @get:Synchronized
        lateinit var context: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

}