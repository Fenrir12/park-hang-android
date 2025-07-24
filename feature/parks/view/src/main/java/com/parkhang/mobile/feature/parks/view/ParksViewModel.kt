package com.parkhang.mobile.feature.parks.view

import androidx.lifecycle.ViewModel
import com.parkhang.mobile.feature.parks.di.statemachine.ParksStateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParksViewModel
    @Inject
    constructor(
        private val parksStateMachine: ParksStateMachine,
    ) : ViewModel() {
        val uiStateFlow = parksStateMachine.uiStateFlow

        fun fetchParks(searchRadius: Int) {
            parksStateMachine.processIntent(
                ParksStateMachine.UiIntent.FetchParks(
                    radius = searchRadius,
                ),
            )
        }
    }
