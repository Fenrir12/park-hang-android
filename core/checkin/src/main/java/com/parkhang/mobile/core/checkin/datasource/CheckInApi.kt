package com.parkhang.mobile.core.checkin.datasource

import com.parkhang.mobile.core.checkin.entity.CheckIn
import com.parkhang.mobile.core.checkin.entity.CheckInDto
import com.parkhang.mobile.framework.network.client.NetworkClient
import com.parkhang.mobile.framework.network.client.model.withResult
import io.ktor.http.HttpMethod
import io.ktor.resources.Resource
import javax.inject.Inject

@Resource("/v1/check-in/{parkId}")
data class CheckIn(
    val parkId: String,
)

class CheckInApi @Inject constructor(
    private val client: NetworkClient,
) {
    suspend fun checkIn(parkId: String): Result<CheckInDto> = withResult {
        client.execute(
            method = HttpMethod.Post,
            resource = CheckIn(parkId = parkId),
        )
    }
}
