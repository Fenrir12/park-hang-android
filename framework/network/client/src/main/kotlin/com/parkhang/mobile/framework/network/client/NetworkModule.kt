package com.parkhang.mobile.framework.network.client

import com.parkhang.mobile.framework.network.client.config.BaseHttpClient
import com.parkhang.mobile.framework.network.client.config.BaseHttpEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun providesOkHttpClient(): BaseHttpEngine = BaseHttpEngine()

    @Provides
    @Singleton
    fun providesBaseHttpClient(baseHttpEngine: BaseHttpEngine): BaseHttpClient = BaseHttpClient(
        baseHttpEngine = baseHttpEngine,
    )
}
