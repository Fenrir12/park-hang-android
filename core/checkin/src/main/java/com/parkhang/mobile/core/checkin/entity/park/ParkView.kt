package com.parkhang.mobile.core.checkin.entity.park

import com.parkhang.mobile.core.common.entity.Park
import com.parkhang.mobile.framework.persistence.datasource.checkinpreferences.LastParkInPersistence
import kotlinx.serialization.Serializable

@Serializable
data class ParkView(
    val id: String,
    val name: String,
    val website: String,
) {
    companion object {
        fun LastParkInPersistence.toParkView(): ParkView = ParkView(
            id = this.id,
            name = this.name,
            website = this.website,
        )

        fun fromPark(park: Park): ParkView = ParkView(
            id = park.id,
            name = park.name.orEmpty(),
            website = park.website.orEmpty(),
        )
    }
}
