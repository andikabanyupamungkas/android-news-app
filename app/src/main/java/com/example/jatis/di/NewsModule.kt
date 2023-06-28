package com.example.jatis.di

import com.example.jatis.remote.Service
import com.example.jatis.repository.Repository
import com.example.jatis.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NewsModule {

    @Singleton
    @Provides
    fun provideSourceApi(api: Retrofit): Service {
        return api.create(Service::class.java)
    }

    @Singleton
    @Provides
    fun provideSourceRepository(api: Service): Repository {
        return RepositoryImpl(api)
    }
}
