package com.parkhang.mobile.feature.view

import androidx.lifecycle.ViewModel
import com.parkhang.mobile.feature.hangout.di.statemachine.HangoutStateMachine
import com.parkhang.mobile.feature.hangout.di.statemachine.HangoutStateMachine.HangoutIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HangoutViewModel @Inject constructor(
    private val hangoutStateMachine: HangoutStateMachine,
) : ViewModel() {
    val uiStateFlow = hangoutStateMachine.hangoutStateFlow

    fun getHangoutsForPark(parkId: String) {
        hangoutStateMachine.processIntent(
            HangoutIntent.DidRequestHangoutsByPark(parkId),
        )
    }
}
