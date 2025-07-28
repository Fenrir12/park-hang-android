package com.parkhang.mobile.core.userprofile.datasource

import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import com.parkhang.mobile.framework.network.client.NetworkClient
import com.parkhang.mobile.framework.network.client.model.NetworkRequestType.Authenticated
import com.parkhang.mobile.framework.network.client.model.withResult
import io.ktor.resources.Resource
import javax.inject.Inject

@Resource("api/users/me")
object MeResource

class UserApi
    @Inject
    constructor(
        private val client: NetworkClient,
    ) {
        suspend fun getMe(): Result<UserProfileInfo> =
            withResult {
                client.execute(
                    resource = MeResource,
                    requestType = Authenticated,
                )
            }
    }
