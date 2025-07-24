package com.parkhang.mobile.feature.parks.di

import com.parkhang.mobile.core.common.Dispatcher
import com.parkhang.mobile.core.common.PHDispatchers
import com.parkhang.mobile.feature.parks.datasource.ParksApi
import com.parkhang.mobile.feature.parks.datasource.ParksRepository
import com.parkhang.mobile.feature.parks.di.statemachine.ParksStateMachine
import com.parkhang.mobile.feature.parks.entity.LatLong
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

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
                parksApi.getNearbyPinsList(
                    latitude = latitude,
                    longitude = longitude,
                    radius = radius,
                )
            },
        )

    @Provides
    @Singleton
    fun providesParksStateMachine(
        parksRepository: ParksRepository,
        @Dispatcher(PHDispatchers.DEFAULT) dispatcher: CoroutineDispatcher,
    ) = ParksStateMachine(
        scope = CoroutineScope(dispatcher),
        getCurrentLocation = {
            LatLong(latitude = 45.54, longitude = -73.55) // Example coordinates for San Francisco
        },
        getNearbyParks = { latitude, longitude, radius ->
            parksRepository.fetchNearbyParks(latitude.toString(), longitude.toString(), radius.toString())
        },
    )
}
