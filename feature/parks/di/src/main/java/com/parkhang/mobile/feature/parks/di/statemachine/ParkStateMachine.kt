package com.parkhang.mobile.feature.parks.di.statemachine

import android.util.Log
import com.parkhang.mobile.feature.parks.entity.LatLong
import com.parkhang.mobile.feature.parks.entity.Park
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
    private val getNearbyParks: suspend (Double, Double, Int) -> List<Park>?,
) {
    data class UiState(
        val userLocation: LatLong? = null,
        val parkList: List<Park> = emptyList(),
        val pinList: List<Pin> = emptyList(),
        val error: String? = null,
        val loading: Boolean = false,
    )

    sealed class UiIntent {
        data class FetchParks(
            val radius: Int,
        ) : UiIntent()

        object GetLocation : UiIntent()
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
                        is UiIntent.FetchParks -> {
                            val currentLocation = getCurrentLocation()
                            fetchParksFlow(
                                latitude = currentLocation.latitude,
                                longitude = currentLocation.longitude,
                                radius = intent.radius,
                            )
                        }
                        is UiIntent.GetLocation -> {
                            flow {
                                emit(
                                    UiState(
                                        userLocation = getCurrentLocation(),
                                    ),
                                )
                            }
                        }
                    }
                }.scan(UiState()) { previousState, newState ->
                    previousState.copy(
                        parkList = if (newState.parkList.isNotEmpty()) newState.parkList else previousState.parkList,
                        pinList = if (newState.pinList.isNotEmpty()) newState.pinList else previousState.pinList,
                        error = newState.error ?: previousState.error,
                        userLocation = newState.userLocation ?: previousState.userLocation,
                    )
                }.stateIn(scope, SharingStarted.Lazily, UiState())
    }

    private fun fetchParksFlow(
        latitude: Double,
        longitude: Double,
        radius: Int,
    ): Flow<UiState> =
        flow {
            emit(UiState(loading = true))
            try {
                val parksList =
                    getNearbyParks(
                        latitude,
                        longitude,
                        radius,
                    )
                if (!parksList.isNullOrEmpty()) {
                    val pinList = parksList.map { Pin.from(it) }
                    emit(
                        UiState(
                            parkList = parksList,
                            pinList = pinList,
                            loading = false,
                            error = null,
                        ),
                    )
                } else {
                    emit(UiState(error = "No parks found", loading = false))
                }
            } catch (e: Exception) {
                Log.e("ParksStateMachine", "Error fetching parks: ${e.message}", e)
                emit(UiState(error = "Error fetching parks: ${e.message}", loading = false))
            }
        }

    fun processIntent(intent: UiIntent) {
        _intents.tryEmit(intent)
    }
}

typealias Longitude = String
typealias Latitude = String
