package com.parkhang.mobile.feature.hangout.entity

import kotlinx.serialization.Serializable

@Serializable
data class Hangout(
    val ownerName: String,
    val title: String,
    val description: String,
    val createdAt: String,
)
