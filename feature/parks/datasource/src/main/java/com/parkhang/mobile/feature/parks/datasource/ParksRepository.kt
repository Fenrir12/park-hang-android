package com.parkhang.mobile.feature.parks.datasource

import com.parkhang.mobile.feature.parks.entity.Park
import com.parkhang.mobile.feature.parks.entity.Pin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ParksRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val getNearbyParksList: suspend (latitude: String, longitude: String, radius: String) -> Result<List<Pin>>,
    private val getNearbyParksById: suspend (parkIdList: List<String>) -> Result<List<Park>>,
) {
    val fetchNearbyParks: suspend (latitude: String, longitude: String, radius: String) -> List<Pin>? =
        { latitude, longitude, radius ->
            withContext(ioDispatcher) {
                getNearbyParksList(latitude, longitude, radius).getOrThrow()
            }
        }

    val fetchParksById: suspend (parkIdList: List<String>) -> List<Park>? =
        { parkIdList ->
            withContext(ioDispatcher) {
                getNearbyParksById(parkIdList).getOrThrow()
            }
        }
}
