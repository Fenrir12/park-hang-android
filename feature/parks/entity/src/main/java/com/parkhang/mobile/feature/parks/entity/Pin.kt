package com.parkhang.mobile.feature.parks.entity

data class Pin(
    val id: String,
    val latLong: LatLong,
) {
    companion object {
        fun from(park: Park): Pin =
            Pin(
                id = park.id,
                latLong =
                    LatLong(
                        longitude = park.geometry.coordinates[0],
                        latitude = park.geometry.coordinates[1],
                    ),
            )
    }
}
