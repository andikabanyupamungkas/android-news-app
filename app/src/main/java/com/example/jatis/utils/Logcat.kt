package com.example.jatis.utils

import android.util.Log
import androidx.multidex.BuildConfig

object Logcat {

    fun d(s: String) {
        if (BuildConfig.DEBUG) {
            Log.d(Const.TAG, s)
        }
    }

    fun e(s: String) {
        if (BuildConfig.DEBUG) {
            Log.e(Const.TAG, s)
        }
    }

    fun e(s: String, t: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(Const.TAG, s, t)
        }
    }


}