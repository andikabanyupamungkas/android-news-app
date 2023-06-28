package com.example.jatis.remote

import com.example.jatis.remote.response.NewsResponse
import com.example.jatis.remote.response.SourceResponse
import com.example.jatis.utils.Const
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("v2/top-headlines")
    suspend fun topHeadlines(
        @Query("apiKey") apiKey: String = Const.API_KEY,
        @Query("sources") source: String,
        @Query("page") page: String,
        @Query("pageSize") pageSize: String,
        @Query("q") search: String?
    ): Response<NewsResponse>

    @GET("v2/top-headlines/sources")
    suspend fun getSources(
        @Query("apiKey") apiKey: String = Const.API_KEY,
        @Query("category") category: String
    ): Response<SourceResponse>
}