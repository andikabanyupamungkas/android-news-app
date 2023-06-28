package com.example.jatis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jatis.models.News
import com.example.jatis.models.Source
import com.example.jatis.remote.response.NewsResponse
import com.example.jatis.usecase.UseCase
import com.example.jatis.utils.DispatcherProvider
import com.example.jatis.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val useCase: UseCase,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private val _articleSource = MutableStateFlow<String?>(null)
    val articleSource = _articleSource.asStateFlow()

    fun setArticleSource(s: String) = viewModelScope.launch {
        _articleSource.value = s
    }

    private val _news = MutableStateFlow<News?>(null)
    val news = _news.asStateFlow()

    fun setNews(news: News) = viewModelScope.launch {
        _news.value = news
    }

    private val _topHeadLines = MutableStateFlow<Resource<NewsResponse>?>(null)
    val topHeadLines = _topHeadLines.asStateFlow()

    fun getTopHeadLines(source: String, page: Int, search: String?) = viewModelScope.launch(dispatcher.io) {
        useCase.getTopHeadLine.invoke(source, page, search).collect {
            _topHeadLines.value = it
        }
    }

    private val _sources = MutableStateFlow<Resource<List<Source>>?>(null)
    val sources = _sources.asStateFlow()

    fun getSources(category: String) = viewModelScope.launch {
        useCase.getNewsSource.invoke(category).collect {
            _sources.value = it
        }
    }
}
