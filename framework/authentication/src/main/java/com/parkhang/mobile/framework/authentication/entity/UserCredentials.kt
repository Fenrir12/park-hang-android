package com.parkhang.mobile.framework.authentication.entity

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(
    val email: String,
    val password: String,
)
