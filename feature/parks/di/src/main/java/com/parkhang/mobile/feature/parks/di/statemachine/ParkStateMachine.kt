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

    private val intents = MutableSharedFlow<UiIntent>(extraBufferCapacity = 1)

    fun processIntent(intent: UiIntent) {
        intents.tryEmit(intent)
    }

    val parksStateFlow: StateFlow<ParksState> = intents
        .flatMapLatest { intent ->
            when (intent) {
                is UiIntent.GetLocation -> getLocationSideEffect()
                is UiIntent.FetchParkPinsNearby -> fetchParksNearbySideEffect(
                    latitude = intent.cameraCenter.latitude,
                    longitude = intent.cameraCenter.longitude,
                    radius = intent.radius,
                )

                is UiIntent.FetchParksByIdList -> fetchParksByIdSideEffect(intent.parkIdList)
            }
        }.scan(ParksState()) { previous, next ->
            previous.copy(
                parkItemList = if (next.parkItemList.isNotEmpty()) next.parkItemList else previous.parkItemList,
                pinList = if (next.pinList.isNotEmpty()) next.pinList else previous.pinList,
                error = next.error ?: previous.error,
                userLocation = next.userLocation ?: previous.userLocation,
            )
        }.logStateTransitionsPretty("ParksState")
        .stateIn(scope, SharingStarted.Eagerly, ParksState())
}
