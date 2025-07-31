package com.parkhang.mobile.core.checkin.datasource

import com.parkhang.mobile.core.checkin.entity.CheckIn
import com.parkhang.mobile.core.checkin.entity.CheckInDto
import com.parkhang.mobile.framework.persistence.datasource.checkinpreferences.LastCheckInPersistence
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CheckInRepository(
    ioDispatcher: CoroutineDispatcher,
    private val checkIn: suspend (parkId: String) -> Result<CheckInDto>,
    private val saveCheckIn: suspend (LastCheckInPersistence) -> Unit,
    private val removeCheckIn: suspend () -> Unit,
    private val getLocalCheckInModel: suspend () -> LastCheckInPersistence?,
    private val isLoggedIn: suspend () -> Boolean,
) {
    val performCheckIn: suspend (parkId: String) -> CheckIn = { parkId ->
        withContext(ioDispatcher) {
            checkIn(parkId)
                .getOrThrow()
                .toCheckIn()
        }
    }

    val saveLastCheckIn: suspend (LastCheckInPersistence) -> Unit = { lastCheckIn ->
        withContext(ioDispatcher) {
            saveCheckIn(lastCheckIn)
        }
    }

    val removeLastCheckIn: suspend () -> Unit = {
        withContext(ioDispatcher) {
            removeCheckIn()
        }
    }

    val getLocalCheckIn: suspend () -> LastCheckInPersistence? = {
        withContext(ioDispatcher) {
            getLocalCheckInModel()
        }
    }

    val isUserLoggedIn: suspend () -> Boolean = {
        withContext(ioDispatcher) {
            isLoggedIn()
        }
    }
}
