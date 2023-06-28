package com.example.jatis.utils

sealed class Resource<T>(val data: T? = null, val error: UiText? = null) {
    class Empty<T>(data: T? = null) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(error: UiText, data: T? = null) : Resource<T>(data, error)
}