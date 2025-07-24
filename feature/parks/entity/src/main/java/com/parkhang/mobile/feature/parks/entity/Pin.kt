package com.parkhang.mobile.feature.parks.entity

import kotlinx.serialization.Serializable

@Serializable
data class Pin(
    val id: String,
    private val geolocation: Geometry,
) {
    val latLong: LatLong
        get() =
            LatLong(
                longitude = geolocation.coordinates[0],
                latitude = geolocation.coordinates[1],
            )
}
