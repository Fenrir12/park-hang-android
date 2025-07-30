package com.parkhang.mobile.feature.parks.di

import com.parkhang.mobile.core.common.Dispatcher
import com.parkhang.mobile.core.common.PHDispatchers
import com.parkhang.mobile.core.common.entity.LatLong
import com.parkhang.mobile.feature.parks.datasource.ParksApi
import com.parkhang.mobile.feature.parks.datasource.ParksRepository
import com.parkhang.mobile.feature.parks.di.statemachine.ParksStateMachine
import com.parkhang.mobile.feature.parks.di.statemachine.sideeffects.FetchParksByIdSideEffect
import com.parkhang.mobile.feature.parks.di.statemachine.sideeffects.FetchParksNearbySideEffect
import com.parkhang.mobile.feature.parks.di.statemachine.sideeffects.GetLocationSideEffect
import com.parkhang.module.framework.geolocation.GeolocationTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ParksModule {
    @Provides
    @Singleton
    fun provideParksRepository(
        parksApi: ParksApi,
        @Dispatcher(PHDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ): ParksRepository = ParksRepository(
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
    fun provideGetLocationSideEffect(
        geolocationTracker: GeolocationTracker,
        @Dispatcher(PHDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ) = GetLocationSideEffect(
        getCurrentLocation = {
            withContext(ioDispatcher) {
                geolocationTracker.geolocation.first().let {
                    LatLong(
                        latitude = it.latitude,
                        longitude = it.longitude,
                    )
                }
            }
        },
    )

    @Provides
    @Singleton
    fun provideFetchParksNearbySideEffect(parksRepository: ParksRepository) = FetchParksNearbySideEffect(
        getNearbyParks = { latitude, longitude, radius ->
            parksRepository.fetchNearbyParks(latitude.toString(), longitude.toString(), radius.toString())
        },
    )

    @Provides
    @Singleton
    fun provideFetchParksByIdSideEffect(
        @Dispatcher(PHDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        geolocationTracker: GeolocationTracker,
        parksRepository: ParksRepository,
    ) = FetchParksByIdSideEffect(
        getParksById = { parkIdList ->
            parksRepository.fetchParksById(parkIdList)
        },
        getCurrentLocation = {
            withContext(ioDispatcher) {
                geolocationTracker.geolocation.first().let {
                    LatLong(
                        latitude = it.latitude,
                        longitude = it.longitude,
                    )
                }
            }
        },
    )

    @Provides
    @Singleton
    fun providesParksStateMachine(
        getLocationSideEffect: GetLocationSideEffect,
        fetchParksNearbySideEffect: FetchParksNearbySideEffect,
        fetchParksByIdSideEffect: FetchParksByIdSideEffect,
        @Dispatcher(PHDispatchers.DEFAULT) dispatcher: CoroutineDispatcher,
    ) = ParksStateMachine(
        scope = CoroutineScope(dispatcher),
        getLocationSideEffect = getLocationSideEffect,
        fetchParksNearbySideEffect = fetchParksNearbySideEffect,
        fetchParksByIdSideEffect = fetchParksByIdSideEffect,
    )
}
