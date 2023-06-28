package com.example.jatis.usecase

import com.example.jatis.models.Source
import com.example.jatis.repository.Repository
import com.example.jatis.utils.Resource
import com.example.jatis.utils.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetSourcesUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(category: String) : Flow<Resource<List<Source>>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.getSources(category)
            if (response.isSuccessful && response.body() != null) {
                response.body()?.let {
                    val list = it.sources ?: emptyList()
                    if (list.isNotEmpty()) {
                        emit(Resource.Success(list))
                    } else {
                        emit(Resource.Empty())
                    }
                }
            } else {
                emit(Resource.Error(UiText.Literal(response.message())))
            }
        } catch (e: IOException) {
            emit(Resource.Error(UiText.Literal(e.localizedMessage ?: "An error occured")))
        }
    }
}