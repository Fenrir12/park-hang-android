package com.parkhang.mobile.feature.parks.di.statemachine.sideeffects

import com.parkhang.mobile.feature.parks.di.statemachine.ParksStateMachine
import com.parkhang.mobile.feature.parks.entity.Pin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.collections.isNullOrEmpty

class FetchParksNearbySideEffect(
    private val getNearbyParks: suspend (Double, Double, Int) -> List<Pin>?,
) {
    operator fun invoke(
        latitude: Double,
        longitude: Double,
        radius: Int,
    ): Flow<ParksStateMachine.ParksState> = flow {
        emit(ParksStateMachine.ParksState(loading = true))
        try {
            val pins = getNearbyParks(latitude, longitude, radius)
            emit(
                if (!pins.isNullOrEmpty()) {
                    ParksStateMachine.ParksState(pinList = pins, loading = false)
                } else {
                    ParksStateMachine.ParksState(error = "No parks found", loading = false)
                },
            )
        } catch (e: Exception) {
            emit(ParksStateMachine.ParksState(error = "Error fetching pins: ${e.message}", loading = false))
        }
    }
}
