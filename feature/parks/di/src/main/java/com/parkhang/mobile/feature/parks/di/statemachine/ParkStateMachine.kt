package com.parkhang.mobile.feature.parks.di.statemachine

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.parkhang.mobile.feature.parks.entity.LatLong
import com.parkhang.mobile.feature.parks.entity.Park
import com.parkhang.mobile.feature.parks.entity.ParkItem
import com.parkhang.mobile.feature.parks.entity.Pin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

class ParksStateMachine(
    scope: CoroutineScope,
    private val getCurrentLocation: suspend () -> LatLong,
    private val getNearbyParks: suspend (Double, Double, Int) -> List<Pin>?,
    private val getParksById: suspend (parkIdList: List<String>) -> List<Park>?,
) {
    data class UiState(
        val userLocation: LatLong? = null,
        val pinList: List<Pin> = emptyList(),
        val parkItemList: List<ParkItem> = emptyList(),
        val error: String? = null,
        val loading: Boolean = false,
    )

    sealed class UiIntent {
        object GetLocation : UiIntent()

        data class FetchParkPinsNearby(
            val radius: Int,
            val cameraCenter: LatLng,
        ) : UiIntent()

        data class FetchParksByIdList(
            val parkIdList: List<String>,
        ) : UiIntent()
    }

    private val _intents = MutableSharedFlow<UiIntent>(extraBufferCapacity = 1)

    val uiStateFlow: StateFlow<UiState>

    init {
        uiStateFlow =
            _intents
                .onStart {
                    emit(
                        UiIntent.GetLocation,
                    )
                }.flatMapLatest { intent ->
                    when (intent) {
                        is UiIntent.GetLocation -> {
                            getLocationFlow()
                        }

                        is UiIntent.FetchParkPinsNearby -> {
                            fetchParksFlow(
                                latitude = intent.cameraCenter.latitude,
                                longitude = intent.cameraCenter.longitude,
                                radius = intent.radius,
                            )
                        }

                        is UiIntent.FetchParksByIdList -> {
                            fetchParkByIdListFlow(
                                parkIdList = intent.parkIdList,
                            )
                        }
                    }
                }.scan(UiState()) { previousState, newState ->
                    previousState.copy(
                        parkItemList = if (newState.parkItemList.isNotEmpty()) newState.parkItemList else previousState.parkItemList,
                        pinList = if (newState.pinList.isNotEmpty()) newState.pinList else previousState.pinList,
                        error = newState.error ?: previousState.error,
                        userLocation = newState.userLocation ?: previousState.userLocation,
                    )
                }.stateIn(scope, SharingStarted.Lazily, UiState())
    }

    fun processIntent(intent: UiIntent) {
        _intents.tryEmit(intent)
    }

    private fun getLocationFlow(): Flow<UiState> =
        flow {
            try {
                emit(
                    UiState(
                        userLocation = getCurrentLocation(),
                    ),
                )
            } catch (e: Exception) {
                Log.e("ParksStateMachine", "Error getting location: ${e.message}", e)
                throw e
            }
        }

    private fun fetchParksFlow(
        latitude: Double,
        longitude: Double,
        radius: Int,
    ): Flow<UiState> =
        flow {
            emit(UiState(loading = true))
            try {
                val pinList =
                    getNearbyParks(
                        latitude,
                        longitude,
                        radius,
                    )
                if (!pinList.isNullOrEmpty()) {
                    emit(
                        UiState(
                            pinList = pinList,
                            loading = false,
                            error = null,
                        ),
                    )
                } else {
                    emit(UiState(error = "No parks found", loading = false))
                }
            } catch (e: Exception) {
                Log.e("ParksStateMachine", "Error fetching pins: ${e.message}", e)
                emit(UiState(error = "Error fetching pins: ${e.message}", loading = false))
            }
        }

    private fun fetchParkByIdListFlow(parkIdList: List<String>): Flow<UiState> =
        flow {
            emit(UiState(loading = true))
            try {
                val parks = getParksById(parkIdList)
                if (parks.isNullOrEmpty()) {
                    emit(UiState(error = "No parks found by ID", loading = false))
                } else {
                    val currentLocation = getCurrentLocation()
                    emit(
                        UiState(
                            parkItemList =
                                parks
                                    .map { park ->
                                        val distance =
                                            distanceBetweenPoints(
                                                LatLng(currentLocation.latitude, currentLocation.longitude),
                                                LatLng(park.location.latitude.toDouble(), park.location.longitude.toDouble()),
                                            )
                                        ParkItem.fromPark(park, distance.toInt())
                                    }.sortedBy { it.distanceFromUser },
                            loading = false,
                        ),
                    )
                }
            } catch (e: Exception) {
                Log.e("ParksStateMachine", "Error fetching parks by ID: ${e.message}", e)
                emit(UiState(error = "Error fetching parks by ID: ${e.message}", loading = false))
            }
        }
}

fun distanceBetweenPoints(
    point1: LatLng,
    point2: LatLng,
): Double {
    return SphericalUtil.computeDistanceBetween(point1, point2) // meters as Double
}

typealias Longitude = String
typealias Latitude = String
