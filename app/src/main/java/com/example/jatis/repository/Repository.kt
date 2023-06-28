package com.example.jatis.repository

import com.example.jatis.remote.response.NewsResponse
import com.example.jatis.remote.response.SourceResponse
import retrofit2.Response

interface Repository {

    suspend fun getTopHeadLines(source: String, page: Int, pageSize: Int, search: String?): Response<NewsResponse>

    suspend fun getSources(category: String): Response<SourceResponse>
}