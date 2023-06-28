package com.example.jatis.remote.response

import android.os.Parcelable
import com.example.jatis.models.Source
import kotlinx.parcelize.Parcelize

@Parcelize
data class SourceResponse(
    val status: String? = null,
    val sources: List<Source>? = null
): Parcelable