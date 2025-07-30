package com.parkhang.mobile.feature.parks.datasource

import com.parkhang.mobile.feature.parks.entity.Park
import com.parkhang.mobile.feature.parks.entity.Pin
import com.parkhang.mobile.framework.network.client.NetworkClient
import com.parkhang.mobile.framework.network.client.model.withResult
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Resource("/v1/pins/nearby")
data class NearbyParkPins(
    val lat: String,
    val lng: String,
    val radius: String,
)

@Resource("/v1/parks")
data class NearbyParksById(
    val limit: Int,
)

@Serializable
data class NearbyParksList(
    val parksIds: List<String>,
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
        ): Result<List<Pin>> = withResult {
            client.execute(
                method = HttpMethod.Get,
                resource =
                    NearbyParkPins(
                        lat = latitude,
                        lng = longitude,
                        radius = radius,
                    ),
            )
        }

        suspend fun getNearbyParksById(parksIdList: List<String>): Result<List<Park>> = withResult {
            client.execute(
                method = HttpMethod.Post,
                resource =
                    NearbyParksById(
                        limit = PARKS_LIMIT,
                    ),
                body =
                    NearbyParksList(
                        parksIds = parksIdList,
                    ),
                headers =
                    arrayOf(
                        getContentTypeJsonHeader(),
                    ),
            )
        }

        private fun getContentTypeJsonHeader(): Pair<String, List<String>> = Pair(HttpHeaders.ContentType, listOf("application/json"))
    }

private const val PARKS_LIMIT = 30
