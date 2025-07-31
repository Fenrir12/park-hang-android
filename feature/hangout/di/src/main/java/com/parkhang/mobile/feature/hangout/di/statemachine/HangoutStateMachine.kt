package com.parkhang.mobile.feature.hangout.di.statemachine

import com.parkhang.mobile.core.common.logStateTransitionsPretty
import com.parkhang.mobile.feature.hangout.di.statemachine.sideeffects.FetchHangoutListInParkSideEffect
import com.parkhang.mobile.feature.hangout.entity.Hangout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class HangoutStateMachine(
    scope: CoroutineScope,
    fetchHangoutListInParkSideEffect: FetchHangoutListInParkSideEffect,
) {
    data class HangoutState(
        val hangoutList: List<Hangout> = emptyList(),
        val isLoading: Boolean = false,
        val isLoggedIn: Boolean? = null,
        val error: String? = null,
    )

    sealed class HangoutIntent {
        data class DidRequestHangoutsByPark(
            val parkId: String,
        ) : HangoutIntent()
    }

    private val intents = MutableSharedFlow<HangoutIntent>(extraBufferCapacity = 1)

    fun processIntent(intent: HangoutIntent) {
        intents.tryEmit(intent)
    }

    val hangoutStateFlow: StateFlow<HangoutState> = intents
        .flatMapLatest { intent ->
            when (intent) {
                is HangoutIntent.DidRequestHangoutsByPark -> {
                    fetchHangoutListInParkSideEffect(intent.parkId)
                }
            }
        }.scan(HangoutState()) { previousState, newState ->
            previousState.copy(
                hangoutList = newState.hangoutList,
                isLoggedIn = newState.isLoggedIn,
                error = newState.error,
            )
        }.logStateTransitionsPretty("HangoutState")
        .stateIn(scope, SharingStarted.Eagerly, HangoutState())
}
