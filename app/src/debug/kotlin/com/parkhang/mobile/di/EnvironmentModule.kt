package com.parkhang.mobile.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class EnvironmentModule {
    @Provides
    fun providesLoadBaseUrl(): LoadBaseUrl =
        LoadBaseUrl(
            loadBaseUrl = { BASE_URL },
        )
}

private const val BASE_URL = "https://park-hang-api-dev.onrender.com/"
