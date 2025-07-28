package com.parkhang.mobile.core.userprofile.entity

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileInfo(
    val email: String,
    val profileName: String = "",
    val name: String = "",
    val surname: String = "",
    val city: String = "",
    val province: String = "",
    val dateOfBirth: String = "",
)
