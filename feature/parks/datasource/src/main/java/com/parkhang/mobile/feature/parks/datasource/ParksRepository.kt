package com.parkhang.mobile.feature.parks.datasource

import com.parkhang.mobile.feature.parks.entity.Parks
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ParksRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val getNearbyParksList: suspend (latitude: String, longitude: String, radius: String) -> Result<Parks>,
) {
    val fetchNearbyParks: suspend (latitude: String, longitude: String, radius: String) -> Parks? =
        { latitude, longitude, radius ->
            withContext(ioDispatcher) {
                getNearbyParksList(latitude, longitude, radius).getOrThrow()
            }
        }
}
