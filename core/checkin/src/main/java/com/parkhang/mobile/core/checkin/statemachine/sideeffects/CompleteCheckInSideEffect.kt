package com.parkhang.mobile.core.checkin.statemachine.sideeffects

import com.parkhang.mobile.core.checkin.statemachine.CheckInStateMachine.CheckInState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CompleteCheckInSideEffect {
    suspend operator fun invoke(): Flow<CheckInState> = flow {
        emit(
            CheckInState(
                checkInIsCompleted = false,
            ),
        )
    }
}
