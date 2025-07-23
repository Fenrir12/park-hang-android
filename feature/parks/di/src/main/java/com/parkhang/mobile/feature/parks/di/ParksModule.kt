package com.parkhang.mobile.feature.parks.di

import com.parkhang.mobile.core.common.Dispatcher
import com.parkhang.mobile.core.common.PHDispatchers
import com.parkhang.mobile.feature.parks.datasource.ParksApi
import com.parkhang.mobile.feature.parks.datasource.ParksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class ParksModule {
    @Provides
    fun provideParksRepository(
        parksApi: ParksApi,
        @Dispatcher(PHDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ): ParksRepository =
        ParksRepository(
            ioDispatcher = ioDispatcher,
            getNearbyParksList = { latitude, longitude, radius ->
                parksApi.getNearbyParksList(
                    latitude = latitude,
                    longitude = longitude,
                    radius = radius,
                )
            },
        )
}
