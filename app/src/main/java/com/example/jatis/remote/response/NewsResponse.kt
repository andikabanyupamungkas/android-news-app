package com.example.jatis.remote.response


import android.os.Parcelable
import com.example.jatis.models.News
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsResponse(
    val articles: List<News>? = null,
    val status: String? = null,
    val totalResults: Int? = null
) : Parcelable