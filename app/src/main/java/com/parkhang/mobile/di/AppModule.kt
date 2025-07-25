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
    fun providesNetworkAdaptRequestFactory(loadBaseUrl: LoadBaseUrl): NetworkAdaptRequestFactory =
        NetworkAdaptRequestFactory(
            loadAccessToken = {
                "JWT_TOKEN" // TODO: Replace with actual token retrieval logic
            },
            loadBaseUrl = { loadBaseUrl() },
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
