package com.parkhang.mobile.framework.authentication.entity

import kotlinx.serialization.Serializable

@Serializable
data class AuthToken(
    val token: String,
)
