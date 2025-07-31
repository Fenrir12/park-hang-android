package com.parkhang.mobile.feature.hangout.di

import com.parkhang.mobile.core.common.Dispatcher
import com.parkhang.mobile.core.common.PHDispatchers
import com.parkhang.mobile.feature.hangout.datasource.HangoutApi
import com.parkhang.mobile.feature.hangout.datasource.HangoutRepository
import com.parkhang.mobile.feature.hangout.di.statemachine.HangoutStateMachine
import com.parkhang.mobile.feature.hangout.di.statemachine.sideeffects.FetchHangoutListInParkSideEffect
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HangoutModule {
    @Provides
    @Singleton
    fun provideHangoutRepository(
        @Dispatcher(PHDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        hangoutApi: HangoutApi,
    ): HangoutRepository = HangoutRepository(
        ioDispatcher = ioDispatcher,
        getHangoutListInPark = { parkId ->
            hangoutApi.checkIn(parkId)
        },
    )

    @Provides
    @Singleton
    fun providesFetchHangoutListInPark(hangoutRepository: HangoutRepository): FetchHangoutListInParkSideEffect = FetchHangoutListInParkSideEffect(
        getHangoutsByParkId = { parkId ->
            hangoutRepository.fetchHangoutListInPark(parkId)
        },
    )

    @Provides
    @Singleton
    fun providesHangoutStateMachine(
        @Dispatcher(PHDispatchers.DEFAULT) dispatcher: CoroutineDispatcher,
        fetchHangoutListInParkSideEffect: FetchHangoutListInParkSideEffect,
    ): HangoutStateMachine = HangoutStateMachine(
        scope = CoroutineScope(dispatcher),
        fetchHangoutListInParkSideEffect = fetchHangoutListInParkSideEffect,
    )
}
