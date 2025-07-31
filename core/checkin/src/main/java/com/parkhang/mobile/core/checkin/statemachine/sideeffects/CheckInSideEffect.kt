package com.parkhang.mobile.core.checkin.statemachine.sideeffects

import com.parkhang.mobile.core.checkin.entity.CheckIn
import com.parkhang.mobile.core.checkin.statemachine.CheckInStateMachine.CheckInState
import com.parkhang.mobile.framework.persistence.datasource.checkinpreferences.LastCheckInPersistence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CheckInSideEffect(
    private val checkIn: suspend (parkId: String) -> CheckIn,
    private val getLastCheckInPersistence: suspend () -> LastCheckInPersistence?,
    private val clearLastCheckIn: suspend () -> Unit,
    private val isLoggedIn: suspend () -> Boolean,
) {
    suspend operator fun invoke(parkId: String): Flow<CheckInState> = flow {
        try {
            val lastCheckIn = getLastCheckInPersistence()
            emit(
                CheckInState(
                    isLoading = true,
                ),
            )
            if (lastCheckIn?.currentParkId == parkId) {
                emit(
                    CheckInState(
                        isAnonymous = lastCheckIn.isAnonymous,
                        checkIn = CheckIn.fromCheckInPersistence(
                            checkInPersistence = lastCheckIn,
                        ),
                        checkInIsCompleted = true,
                        checkInFailure = "You have already checked in at this park.",
                        isLoading = false,
                    ),
                )
                return@flow
            }
            clearLastCheckIn()
            val result = checkIn(parkId)
            emit(
                CheckInState(
                    checkIn = result,
                    isAnonymous = isLoggedIn().not(),
                    checkInIsCompleted = true,
                    isLoading = false,
                    checkInFailure = null,
                ),
            )
        } catch (e: Exception) {
            emit(CheckInState(checkInFailure = e.message))
        }
    }
}
