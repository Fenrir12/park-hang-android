package com.parkhang.mobile.core.userprofile.datasource

import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import com.parkhang.mobile.framework.network.client.NetworkClient
import com.parkhang.mobile.framework.network.client.model.NetworkRequestType.Authenticated
import com.parkhang.mobile.framework.network.client.model.withResult
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.resources.Resource
import javax.inject.Inject

@Resource("/v1/users/me")
object MeResource

class UserApi
    @Inject
    constructor(
        private val client: NetworkClient,
    ) {
        suspend fun getMe(): Result<UserProfileInfo> = withResult {
            client.execute(
                resource = MeResource,
                requestType = Authenticated,
            )
        }

        suspend fun patchMe(userProfileInfo: UserProfileInfo): Result<UserProfileInfo> = withResult {
            client.execute(
                method = HttpMethod.Patch,
                body = userProfileInfo,
                resource = MeResource,
                requestType = Authenticated,
                headers = arrayOf(getContentTypeJsonHeader()),
            )
        }

        private fun getContentTypeJsonHeader(): Pair<String, List<String>> = Pair(HttpHeaders.ContentType, listOf("application/json"))
    }
