package com.parkhang.mobile.feature.hangout.datasource

import com.parkhang.mobile.feature.hangout.entity.Hangout
import com.parkhang.mobile.framework.network.client.NetworkClient
import com.parkhang.mobile.framework.network.client.model.withResult
import io.ktor.http.HttpMethod
import io.ktor.resources.Resource
import javax.inject.Inject

@Resource("/v1/hangouts/{parkId}")
data class Hangouts(
    val parkId: String,
)

class HangoutApi @Inject constructor(
    private val client: NetworkClient,
) {
    suspend fun checkIn(parkId: String): Result<List<Hangout>> = withResult {
        client.execute(
            method = HttpMethod.Get,
            resource = Hangouts(parkId = parkId),
        )
    }
}
