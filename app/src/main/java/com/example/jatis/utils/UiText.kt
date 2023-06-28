package com.example.jatis.utils

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {
    data class Literal(val value: String) : UiText()
    class FromResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    fun asString(context: Context) : String {
        return when(this) {
            is Literal -> value
            is FromResource -> context.getString(resId, *args)
        }
    }
}