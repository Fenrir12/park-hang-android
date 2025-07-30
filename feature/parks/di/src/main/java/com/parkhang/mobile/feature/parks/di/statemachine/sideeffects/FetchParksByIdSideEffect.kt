package com.parkhang.mobile.feature.parks.di.statemachine.sideeffects

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.parkhang.mobile.core.common.entity.LatLong
import com.parkhang.mobile.core.common.entity.Park
import com.parkhang.mobile.feature.parks.di.statemachine.ParksStateMachine
import com.parkhang.mobile.feature.parks.entity.ParkItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchParksByIdSideEffect(
    private val getParksById: suspend (List<String>) -> List<Park>?,
    private val getCurrentLocation: suspend () -> LatLong,
) {
    operator fun invoke(parkIdList: List<String>): Flow<ParksStateMachine.ParksState> = flow {
        emit(ParksStateMachine.ParksState(loading = true))
        try {
            val parks = getParksById(parkIdList)
            if (parks.isNullOrEmpty()) {
                emit(ParksStateMachine.ParksState(error = "No parks found by ID", loading = false))
            } else {
                val location = getCurrentLocation()
                val items = parks
                    .map { park ->
                        val distance = distanceBetweenPoints(
                            LatLng(location.latitude, location.longitude),
                            LatLng(park.location.latitude.toDouble(), park.location.longitude.toDouble()),
                        )
                        ParkItem.fromPark(park, distance.toInt())
                    }.sortedBy { it.distanceFromUser }

                emit(ParksStateMachine.ParksState(parkItemList = items, loading = false))
            }
        } catch (e: Exception) {
            emit(ParksStateMachine.ParksState(error = "Error fetching parks by ID: ${e.message}", loading = false))
        }
    }
}

fun distanceBetweenPoints(
    point1: LatLng,
    point2: LatLng,
): Double {
    return SphericalUtil.computeDistanceBetween(point1, point2) // meters as Double
}
