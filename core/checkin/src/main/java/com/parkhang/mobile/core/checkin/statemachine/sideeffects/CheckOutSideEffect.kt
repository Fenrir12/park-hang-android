package com.parkhang.mobile.core.checkin.statemachine.sideeffects

import com.parkhang.mobile.core.checkin.statemachine.CheckInStateMachine.CheckInState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CheckOutSideEffect(
    private val clearLastCheckIn: suspend () -> Unit,
) {
    suspend operator fun invoke(): Flow<CheckInState> = flow {
        clearLastCheckIn()
        emit(
            CheckInState(
                isAnonymous = false,
                checkIn = null,
                checkInIsCompleted = false,
                checkInFailure = null,
            ),
        )
    }
}
