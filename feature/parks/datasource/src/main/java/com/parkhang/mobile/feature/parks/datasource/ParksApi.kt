package com.parkhang.mobile.feature.parks.datasource

import com.parkhang.mobile.feature.parks.entity.Parks
import com.parkhang.mobile.framework.network.client.NetworkClient
import com.parkhang.mobile.framework.network.client.model.withResult
import io.ktor.http.HttpMethod
import io.ktor.resources.Resource
import javax.inject.Inject

@Resource("/api/parks/nearby")
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
        suspend fun getNearbyParksList(
            latitude: String,
            longitude: String,
            radius: String,
        ): Result<Parks> =
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
