package com.parkhang.mobile.feature.parks.di.statemachine

import com.google.android.gms.maps.model.LatLng
import com.parkhang.mobile.core.common.entity.LatLong
import com.parkhang.mobile.core.common.logStateTransitionsPretty
import com.parkhang.mobile.feature.parks.di.statemachine.sideeffects.FetchParksByIdSideEffect
import com.parkhang.mobile.feature.parks.di.statemachine.sideeffects.FetchParksNearbySideEffect
import com.parkhang.mobile.feature.parks.di.statemachine.sideeffects.GetLocationSideEffect
import com.parkhang.mobile.feature.parks.entity.ParkItem
import com.parkhang.mobile.feature.parks.entity.Pin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.Serializable

class ParksStateMachine(
    scope: CoroutineScope,
    private val getLocationSideEffect: GetLocationSideEffect,
    private val fetchParksNearbySideEffect: FetchParksNearbySideEffect,
    private val fetchParksByIdSideEffect: FetchParksByIdSideEffect,
) {
    @Serializable
    data class ParksState(
        val userLocation: LatLong? = null,
        val pinList: List<Pin> = emptyList(),
        val parkItemList: List<ParkItem> = emptyList(),
        val selectedPinId: String? = null,
        val error: String? = null,
        val loading: Boolean = false,
    )

    sealed class ParkIntent {
        object GetLocation : ParkIntent()

        data class DidFetchParkPinsNearby(
            val radius: Int,
            val cameraCenter: LatLng,
        ) : ParkIntent()

        data class DidFetchParksByIdList(
            val parkIdList: List<String>,
        ) : ParkIntent()

        data class DidSelectPin(
            val pinId: String,
        ) : ParkIntent()

        data object DidUnselectPin : ParkIntent()
    }

    private val intents = MutableSharedFlow<ParkIntent>(extraBufferCapacity = 1)

    fun processIntent(intent: ParkIntent) {
        intents.tryEmit(intent)
    }

    val parksStateFlow: StateFlow<ParksState> = intents
        .flatMapLatest { intent ->
            when (intent) {
                is ParkIntent.GetLocation -> {
                    getLocationSideEffect()
                }
                is ParkIntent.DidFetchParkPinsNearby -> {
                    fetchParksNearbySideEffect(
                        latitude = intent.cameraCenter.latitude,
                        longitude = intent.cameraCenter.longitude,
                        radius = intent.radius,
                    )
                }
                is ParkIntent.DidFetchParksByIdList -> {
                    fetchParksByIdSideEffect(intent.parkIdList)
                }
                is ParkIntent.DidSelectPin -> {
                    selectPinQuickEffect(
                        pinId = intent.pinId,
                    )
                }
                is ParkIntent.DidUnselectPin -> {
                    unselectPinQuickEffect()
                }
            }
        }.scan(ParksState()) { previous, next ->
            previous.copy(
                parkItemList = if (next.parkItemList.isNotEmpty()) next.parkItemList else previous.parkItemList,
                pinList = if (next.pinList.isNotEmpty()) next.pinList else previous.pinList,
                selectedPinId = next.selectedPinId,
                userLocation = next.userLocation ?: previous.userLocation,
                error = next.error ?: previous.error,
            )
        }.logStateTransitionsPretty("ParksState")
        .stateIn(scope, SharingStarted.Eagerly, ParksState())

    private fun selectPinQuickEffect(pinId: String) = flow {
        emit(
            ParksState(
                selectedPinId = pinId,
            ),
        )
    }

    private fun unselectPinQuickEffect() = flow {
        emit(
            ParksState(
                selectedPinId = null,
            ),
        )
    }
}
