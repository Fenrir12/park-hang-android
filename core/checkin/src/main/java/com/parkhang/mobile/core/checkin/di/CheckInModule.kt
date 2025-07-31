package com.parkhang.mobile.core.checkin.di

import com.parkhang.mobile.core.checkin.datasource.CheckInApi
import com.parkhang.mobile.core.checkin.datasource.CheckInRepository
import com.parkhang.mobile.core.checkin.statemachine.CheckInStateMachine
import com.parkhang.mobile.core.checkin.statemachine.sideeffects.CheckInSideEffect
import com.parkhang.mobile.core.checkin.statemachine.sideeffects.CheckOutSideEffect
import com.parkhang.mobile.core.checkin.statemachine.sideeffects.CompleteCheckInSideEffect
import com.parkhang.mobile.core.common.Dispatcher
import com.parkhang.mobile.core.common.PHDispatchers
import com.parkhang.mobile.core.event.AppEventBus
import com.parkhang.mobile.framework.authentication.AuthenticationFramework
import com.parkhang.mobile.framework.persistence.datasource.checkinpreferences.CheckInDatasource
import com.parkhang.mobile.framework.persistence.datasource.checkinpreferences.LastCheckInPersistence
import com.parkhang.mobile.framework.persistence.datasource.checkinpreferences.LastParkInPersistence
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CheckInModule {
    @Provides
    @Singleton
    fun providesCheckInRepository(
        @Dispatcher(PHDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        checkInApi: CheckInApi,
        checkInDatasource: CheckInDatasource,
        authenticationFramework: AuthenticationFramework,
    ): CheckInRepository = CheckInRepository(
        ioDispatcher = ioDispatcher,
        checkIn = { parkId ->
            checkInApi.checkIn(parkId)
        },
        removeCheckIn = {
            checkInDatasource.removeLastCheckIn()
        },
        saveCheckIn = { lastCheckIn ->
            checkInDatasource.updateLastCheckIn(lastCheckIn)
        },
        getLocalCheckInModel = {
            checkInDatasource.getLastCheckInPersistence.firstOrNull()
        },
        isLoggedIn = { authenticationFramework.isUserLoggedIn() },
    )

    @Provides
    @Singleton
    fun providesCheckInSideEffect(checkInRepository: CheckInRepository): CheckInSideEffect = CheckInSideEffect(
        checkIn = { parkId ->
            checkInRepository.performCheckIn(parkId).also {
                checkInRepository.saveLastCheckIn(
                    LastCheckInPersistence(
                        currentParkId = parkId,
                        timestamp = it.timestamp,
                        park = LastParkInPersistence(
                            id = it.currentParkView.id,
                            name = it.currentParkView.name,
                            website = it.currentParkView.website,
                        ),
                        isAnonymous = checkInRepository.isUserLoggedIn().not(),
                    ),
                )
            }
        },
        getLastCheckInPersistence = {
            checkInRepository.getLocalCheckIn()
        },
        clearLastCheckIn = {
            checkInRepository.removeLastCheckIn()
        },
        isLoggedIn = {
            checkInRepository.isUserLoggedIn()
        },
    )

    @Provides
    fun providesCompleteCheckInSideEffect(): CompleteCheckInSideEffect = CompleteCheckInSideEffect()

    @Provides
    fun providesCheckOutSideEffect(checkInRepository: CheckInRepository): CheckOutSideEffect = CheckOutSideEffect(
        clearLastCheckIn = {
            checkInRepository.removeLastCheckIn()
        },
    )

    @Provides
    @Singleton
    fun providesCheckInStateMachine(
        @Dispatcher(PHDispatchers.DEFAULT) defaultDispatcher: CoroutineDispatcher,
        checkInSideEffect: CheckInSideEffect,
        completeCheckInSideEffect: CompleteCheckInSideEffect,
        checkOutSideEffect: CheckOutSideEffect,
        appEventBus: AppEventBus,
    ): CheckInStateMachine = CheckInStateMachine(
        scope = CoroutineScope(defaultDispatcher),
        checkInSideEffect = checkInSideEffect,
        completeCheckInSideEffect = completeCheckInSideEffect,
        checkOutSideEffect = checkOutSideEffect,
        appEventBus = appEventBus,
    )
}
