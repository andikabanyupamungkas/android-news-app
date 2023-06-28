package com.example.jatis.ui

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.jatis.ui.event.MainEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragment: Fragment() {

    protected var mainEvent: MainEvent? = null

    protected fun setMainEvent(context: Context) {
        if (context is MainEvent) context.also { mainEvent = it }
    }
}
