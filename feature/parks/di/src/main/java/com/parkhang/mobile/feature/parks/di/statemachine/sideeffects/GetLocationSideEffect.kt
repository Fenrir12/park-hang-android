package com.parkhang.mobile.feature.parks.di.statemachine.sideeffects

import com.parkhang.mobile.core.common.entity.LatLong
import com.parkhang.mobile.feature.parks.di.statemachine.ParksStateMachine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLocationSideEffect(
    private val getCurrentLocation: suspend () -> LatLong,
) {
    operator fun invoke(): Flow<ParksStateMachine.ParksState> = flow {
        try {
            emit(
                ParksStateMachine.ParksState(
                    userLocation = getCurrentLocation(),
                ),
            )
        } catch (e: Exception) {
            emit(
                ParksStateMachine.ParksState(
                    error = "Error getting location: ${e.message}",
                ),
            )
        }
    }
}
