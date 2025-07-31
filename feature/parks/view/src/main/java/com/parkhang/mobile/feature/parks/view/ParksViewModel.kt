package com.parkhang.mobile.feature.parks.view

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.parkhang.mobile.core.checkin.statemachine.CheckInStateMachine
import com.parkhang.mobile.feature.parks.di.statemachine.ParksStateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParksViewModel
    @Inject
    constructor(
        private val parksStateMachine: ParksStateMachine,
        private val checkInStateMachine: CheckInStateMachine,
    ) : ViewModel() {
        val uiStateFlow = parksStateMachine.parksStateFlow
        val checkInStateFlow = checkInStateMachine.didRequestCheckInStateFlow

        fun getUserLocation() {
            parksStateMachine.processIntent(
                ParksStateMachine.ParkIntent.GetLocation,
            )
        }

        fun fetchPins(
            cameraCenter: LatLng,
            searchRadius: Int,
        ) {
            parksStateMachine.processIntent(
                ParksStateMachine.ParkIntent.DidFetchParkPinsNearby(
                    cameraCenter = cameraCenter,
                    radius = searchRadius,
                ),
            )
        }

        fun fetchParkByIds(parkIdList: List<String>) {
            parksStateMachine.processIntent(
                ParksStateMachine.ParkIntent.DidFetchParksByIdList(
                    parkIdList = parkIdList,
                ),
            )
        }

        fun getParkById(parkId: String) {
            checkInStateMachine.processIntent(
                CheckInStateMachine.Intent.DidRequestCheckIn(
                    parkId = parkId,
                ),
            )
        }

        fun onCheckInPerformed() {
            checkInStateMachine.processIntent(
                CheckInStateMachine.Intent.DidCompleteCheckIn,
            )
        }

        fun onSelectPin(pinId: String) {
            parksStateMachine.processIntent(
                ParksStateMachine.ParkIntent.DidSelectPin(
                    pinId = pinId,
                ),
            )
        }

        fun onUnselectPin() {
            parksStateMachine.processIntent(
                ParksStateMachine.ParkIntent.DidUnselectPin,
            )
        }
    }
