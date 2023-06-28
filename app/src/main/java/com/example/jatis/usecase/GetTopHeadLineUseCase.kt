package com.example.jatis.usecase

import com.example.jatis.remote.response.NewsResponse
import com.example.jatis.repository.Repository
import com.example.jatis.utils.Const
import com.example.jatis.utils.Resource
import com.example.jatis.utils.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetTopHeadLineUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(source: String, page: Int, search: String?) : Flow<Resource<NewsResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.getTopHeadLines(source, page, Const.PAGE_SIZE, search)
            if (response.isSuccessful && response.body() != null) {
                response.body()?.let {
                    emit(Resource.Success(it))
                }
            } else {
                emit(Resource.Error(UiText.Literal(response.message())))
            }
        } catch (e: IOException) {
            emit(Resource.Error(UiText.Literal(e.localizedMessage ?: "An error occured")))
        }
    }
}
