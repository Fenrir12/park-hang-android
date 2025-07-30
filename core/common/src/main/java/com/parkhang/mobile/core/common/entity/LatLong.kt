package com.parkhang.mobile.core.common.entity

import kotlinx.serialization.Serializable

@Serializable
data class LatLong(
    val latitude: Double,
    val longitude: Double,
)
