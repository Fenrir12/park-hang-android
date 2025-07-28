package com.parkhang.mobile.di

import android.util.Log
import com.parkhang.mobile.framework.network.client.NetworkClient
import com.parkhang.mobile.framework.network.client.config.BaseHttpClient
import com.parkhang.mobile.framework.network.client.model.NetworkAdaptRequestFactory
import com.parkhang.mobile.framework.persistence.datasource.usercredentialspreferences.UserCredentialsDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun providesNetworkAdaptRequestFactory(
        loadBaseUrl: LoadBaseUrl,
        userCredentialsDatasource: UserCredentialsDatasource,
    ): NetworkAdaptRequestFactory =
        NetworkAdaptRequestFactory(
            loadAccessToken = {
                val token = userCredentialsDatasource.getUserAuthToken().first()?.accessToken ?: ""
                Log.d("NetworkAdaptRequestFactory", "Loaded access token: $token")
                token
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
