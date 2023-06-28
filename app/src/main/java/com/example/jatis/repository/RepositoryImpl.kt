package com.example.jatis.repository

import com.example.jatis.remote.response.NewsResponse
import com.example.jatis.remote.Service
import com.example.jatis.remote.response.SourceResponse
import com.example.jatis.utils.Const
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: Service
) : Repository {

    override suspend fun getTopHeadLines(
        source: String,
        page: Int,
        pageSize: Int,
        search: String?
    ): Response<NewsResponse> {
        return api.topHeadlines(Const.API_KEY, source, page.toString(), pageSize.toString(), search)
    }

    override suspend fun getSources(category: String): Response<SourceResponse> {
        return api.getSources(Const.API_KEY, category)
    }
}
