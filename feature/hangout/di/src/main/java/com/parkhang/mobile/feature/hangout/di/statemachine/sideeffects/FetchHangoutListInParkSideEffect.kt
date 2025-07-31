package com.parkhang.mobile.feature.hangout.di.statemachine.sideeffects

import com.parkhang.mobile.feature.hangout.di.statemachine.HangoutStateMachine
import com.parkhang.mobile.feature.hangout.entity.Hangout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchHangoutListInParkSideEffect(
    private val getHangoutsByParkId: suspend (String) -> List<Hangout>,
) {
    operator fun invoke(parkId: String): Flow<HangoutStateMachine.HangoutState> = flow {
        emit(HangoutStateMachine.HangoutState(isLoading = true))
        try {
            val hangouts = getHangoutsByParkId(parkId)
            if (hangouts.isEmpty()) {
                emit(HangoutStateMachine.HangoutState(error = "No hangouts found by ID", isLoading = false))
            } else {
                emit(HangoutStateMachine.HangoutState(hangoutList = hangouts, isLoading = false))
            }
        } catch (e: Exception) {
            emit(HangoutStateMachine.HangoutState(error = "Error fetching parks by ID: ${e.message}", isLoading = false))
        }
    }
}
