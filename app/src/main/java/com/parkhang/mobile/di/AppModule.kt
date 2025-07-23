package com.parkhang.mobile.di

import com.parkhang.mobile.framework.network.client.NetworkClient
import com.parkhang.mobile.framework.network.client.config.BaseHttpClient
import com.parkhang.mobile.framework.network.client.model.NetworkAdaptRequestFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun providesNetworkAdaptRequestFactory(): NetworkAdaptRequestFactory =
        NetworkAdaptRequestFactory(
            loadAccessToken = {
                "JWT_TOKEN" // TODO: Replace with actual token retrieval logic
            },
            loadBaseUrl = { BASE_URL },
        )

    @Provides
    fun providesNetworkClient(
        baseHttpClient: BaseHttpClient,
        networkAdaptRequestFactory: NetworkAdaptRequestFactory,
    ): NetworkClient =
        NetworkClient(
            client = baseHttpClient.build(),
            adaptRequest = { networkAdaptRequestFactory },
        )
}

private const val BASE_URL = "https://192.168.18.2:5555/"
