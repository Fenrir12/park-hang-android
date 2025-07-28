package com.parkhang.mobile.framework.authentication

import com.auth0.jwt.JWT
import com.parkhang.mobile.framework.persistence.datasource.usercredentialspreferences.UserAuthToken
import com.parkhang.mobile.framework.persistence.datasource.usercredentialspreferences.UserProfile
import java.util.Date

fun String.decodeJwt(): UserAuthToken {
    val decoded = JWT.decode(this)

    val id = decoded.id ?: ""
    val expiresAt = decoded.expiresAt ?: Date(0)

    val user =
        UserProfile(
            id = decoded.getClaim("id").asString(),
            email = decoded.getClaim("email").asString(),
        )

    return UserAuthToken(
        idToken = id,
        accessToken = this,
        expiresAt = expiresAt,
        user = user,
    )
}
