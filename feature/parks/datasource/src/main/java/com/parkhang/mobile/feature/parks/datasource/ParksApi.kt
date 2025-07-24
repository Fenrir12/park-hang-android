package com.parkhang.mobile.feature.parks.datasource

import com.parkhang.mobile.feature.parks.entity.Pin
import com.parkhang.mobile.framework.network.client.NetworkClient
import com.parkhang.mobile.framework.network.client.model.withResult
import io.ktor.http.HttpMethod
import io.ktor.resources.Resource
import javax.inject.Inject

@Resource("/api/pins/nearby")
data class NearbyParks(
    val lat: String,
    val lng: String,
    val radius: String,
)

class ParksApi
    @Inject
    constructor(
        private val client: NetworkClient,
    ) {
        suspend fun getNearbyPinsList(
            latitude: String,
            longitude: String,
            radius: String,
        ): Result<List<Pin>> =
            withResult {
                client.execute(
                    method = HttpMethod.Get,
                    resource =
                        NearbyParks(
                            lat = latitude,
                            lng = longitude,
                            radius = radius,
                        ),
                )
            }
    }
