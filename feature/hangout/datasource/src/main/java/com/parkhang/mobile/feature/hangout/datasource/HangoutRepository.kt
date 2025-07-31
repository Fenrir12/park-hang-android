package com.parkhang.mobile.feature.hangout.datasource

import com.parkhang.mobile.feature.hangout.entity.Hangout
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HangoutRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val getHangoutListInPark: suspend (parkId: String) -> Result<List<Hangout>>,
) {
    suspend fun fetchHangoutListInPark(parkId: String): List<Hangout> = withContext(ioDispatcher) {
        getHangoutListInPark(parkId).getOrThrow()
    }
}
