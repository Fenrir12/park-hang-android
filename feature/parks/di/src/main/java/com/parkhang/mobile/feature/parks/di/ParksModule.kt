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
    fun providesUserLocation(): LatLong =
        LatLong(latitude = 45.547967672732746, longitude = -73.54622861361825) // TODO: Replace with actual location fetching framework

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
            getNearbyParksById = { parkIdList ->
                parksApi.getNearbyParksById(parkIdList)
            },
        )

    @Provides
    @Singleton
    fun providesParksStateMachine(
        userLocation: LatLong,
        parksRepository: ParksRepository,
        @Dispatcher(PHDispatchers.DEFAULT) dispatcher: CoroutineDispatcher,
    ) = ParksStateMachine(
        scope = CoroutineScope(dispatcher),
        getCurrentLocation = { userLocation },
        getNearbyParks = { latitude, longitude, radius ->
            parksRepository.fetchNearbyParks(latitude.toString(), longitude.toString(), radius.toString())
        },
        getParksById = { parkIdList ->
            parksRepository.fetchParksById(parkIdList)
        },
    )
}
