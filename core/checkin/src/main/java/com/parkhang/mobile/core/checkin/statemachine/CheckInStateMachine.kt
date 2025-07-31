package com.parkhang.mobile.core.checkin.statemachine

import com.parkhang.mobile.core.checkin.entity.CheckIn
import com.parkhang.mobile.core.checkin.statemachine.sideeffects.CheckInSideEffect
import com.parkhang.mobile.core.checkin.statemachine.sideeffects.CheckOutSideEffect
import com.parkhang.mobile.core.checkin.statemachine.sideeffects.CompleteCheckInSideEffect
import com.parkhang.mobile.core.common.logStateTransitionsPretty
import com.parkhang.mobile.core.event.AppEventBus
import com.parkhang.mobile.core.event.EventModule.AppEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CheckInStateMachine(
    scope: CoroutineScope,
    checkInSideEffect: CheckInSideEffect,
    completeCheckInSideEffect: CompleteCheckInSideEffect,
    checkOutSideEffect: CheckOutSideEffect,
    appEventBus: AppEventBus,
) {
    data class CheckInState(
        val isLoading: Boolean = false,
        val isAnonymous: Boolean = false,
        val checkIn: CheckIn? = null,
        val checkInIsCompleted: Boolean = false,
        val checkInFailure: String? = null,
    )

    sealed class Intent {
        data class DidRequestCheckIn(
            val parkId: String,
        ) : Intent()

        data object DidRequestCheckout : Intent()

        data object DidCompleteCheckIn : Intent()
    }

    private val intents = MutableSharedFlow<Intent>(extraBufferCapacity = 1)

    fun processIntent(intent: Intent) {
        intents.tryEmit(intent)
    }

    val didRequestCheckInStateFlow: StateFlow<CheckInState> = intents
        .flatMapLatest { intent ->
            when (intent) {
                is Intent.DidRequestCheckIn -> {
                    checkInSideEffect(intent.parkId)
                }
                Intent.DidCompleteCheckIn -> {
                    completeCheckInSideEffect()
                }
                Intent.DidRequestCheckout -> {
                    checkOutSideEffect()
                }
            }
        }.scan(CheckInState()) { previousState, newState ->
            previousState.copy(
                checkIn = newState.checkIn ?: previousState.checkIn,
                checkInFailure = newState.checkInFailure,
                checkInIsCompleted = newState.checkInIsCompleted,
                isAnonymous = newState.isAnonymous,
                isLoading = newState.isLoading,
            )
        }.logStateTransitionsPretty(tag = "CheckInStateMachine")
        .stateIn(scope, SharingStarted.Eagerly, CheckInState())

    init {
        scope.launch {
            appEventBus.events
                .filterIsInstance<AppEvent.UserLoggedOut>()
                .collect { event ->
                    processIntent(Intent.DidRequestCheckout)
                }
        }
    }
}
