package com.parkhang.mobile.feature.parks.datasource

import com.parkhang.mobile.feature.parks.entity.Park
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ParksRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val getNearbyParksList: suspend (latitude: String, longitude: String, radius: String) -> Result<List<Park>>,
) {
    val fetchNearbyParks: suspend (latitude: String, longitude: String, radius: String) -> List<Park>? =
        { latitude, longitude, radius ->
            withContext(ioDispatcher) {
                getNearbyParksList(latitude, longitude, radius).getOrThrow()
            }
        }
}
