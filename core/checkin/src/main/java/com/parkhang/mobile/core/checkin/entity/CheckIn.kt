package com.parkhang.mobile.core.checkin.entity

import com.parkhang.mobile.core.checkin.entity.park.ParkView
import com.parkhang.mobile.core.checkin.entity.park.ParkView.Companion.fromPark
import com.parkhang.mobile.core.checkin.entity.park.ParkView.Companion.toParkView
import com.parkhang.mobile.core.common.entity.Park
import com.parkhang.mobile.framework.persistence.datasource.checkinpreferences.LastCheckInPersistence
import kotlinx.serialization.Serializable

@Serializable
data class CheckInDto(
    val checkInId: String?,
    val isAnonymous: Boolean,
    val currentPark: Park,
    val timestamp: String,
) {
    fun toCheckIn(): CheckIn = CheckIn(
        checkInId = checkInId,
        isAnonymous = isAnonymous,
        currentParkView = fromPark(currentPark),
        timestamp = timestamp,
    )
}

@Serializable
data class CheckIn(
    val checkInId: String?,
    val isAnonymous: Boolean,
    val currentParkView: ParkView,
    val timestamp: String,
) {
    companion object {
        fun fromCheckInPersistence(checkInPersistence: LastCheckInPersistence): CheckIn = CheckIn(
            checkInId = null,
            isAnonymous = checkInPersistence.isAnonymous,
            currentParkView = checkInPersistence.park.toParkView(),
            timestamp = checkInPersistence.timestamp,
        )
    }
}
