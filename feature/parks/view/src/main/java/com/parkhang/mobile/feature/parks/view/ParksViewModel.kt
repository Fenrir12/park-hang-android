package com.parkhang.mobile.feature.parks.view

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
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

        fun fetchPins(
            cameraCenter: LatLng,
            searchRadius: Int,
        ) {
            parksStateMachine.processIntent(
                ParksStateMachine.UiIntent.FetchParkPinsNearby(
                    cameraCenter = cameraCenter,
                    radius = searchRadius,
                ),
            )
        }

        fun fetchParkByIds(parkIdList: List<String>) {
            parksStateMachine.processIntent(
                ParksStateMachine.UiIntent.FetchParksByIdList(
                    parkIdList = parkIdList,
                ),
            )
        }
    }
