package com.parkhang.mobile.feature.parks.entity

data class ParkItem(
    val id: String,
    val name: String? = null,
    val distanceFromUser: Int,
) {
    companion object {
        fun fromPark(
            park: Park,
            distanceFromUser: Int,
        ): ParkItem = ParkItem(
            id = park.id,
            name = park.name,
            distanceFromUser = distanceFromUser,
        )
    }
}
