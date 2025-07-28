package com.parkhang.mobile.framework.authentication

import com.parkhang.mobile.framework.authentication.entity.AuthToken
import com.parkhang.mobile.framework.authentication.entity.UserCredentials
import com.parkhang.mobile.framework.network.client.NetworkClient
import com.parkhang.mobile.framework.network.client.model.withResult
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.resources.Resource
import javax.inject.Inject

@Resource("/api/auth/signup")
data object SignUp

@Resource("/api/auth/login")
data object Login

class AuthenticationClient
    @Inject
    constructor(
        private val client: NetworkClient,
    ) {
        suspend fun signUp(credentials: UserCredentials): Result<AuthToken> =
            withResult {
                client.execute(
                    method = HttpMethod.Post,
                    resource = SignUp,
                    body = credentials,
                    headers = arrayOf(getContentTypeJsonHeader()),
                )
            }

        suspend fun login(credentials: UserCredentials): Result<AuthToken> =
            withResult {
                client.execute(
                    method = HttpMethod.Post,
                    resource = Login,
                    body = credentials,
                    headers = arrayOf(getContentTypeJsonHeader()),
                )
            }

        private fun getContentTypeJsonHeader(): Pair<String, List<String>> = Pair(HttpHeaders.ContentType, listOf("application/json"))
    }
