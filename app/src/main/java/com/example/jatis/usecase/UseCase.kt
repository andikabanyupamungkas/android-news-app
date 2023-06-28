package com.example.jatis.usecase

data class UseCase(
    val getNewsSource: GetSourcesUseCase,
    val getTopHeadLine: GetTopHeadLineUseCase
)
