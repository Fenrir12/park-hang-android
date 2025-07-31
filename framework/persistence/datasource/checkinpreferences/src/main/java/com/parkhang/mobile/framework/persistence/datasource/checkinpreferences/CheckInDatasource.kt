package com.parkhang.mobile.framework.persistence.datasource.checkinpreferences

import androidx.datastore.core.DataStore
import com.parkhang.mobile.framework.persistence.localdatastorage.CheckIn
import com.parkhang.mobile.framework.persistence.localdatastorage.CheckInPreferences
import com.parkhang.mobile.framework.persistence.localdatastorage.Park
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class CheckInDatasource
    @Inject
    constructor(
        private val checkInPreferencesStore: DataStore<CheckInPreferences>,
        private val logError: (Throwable) -> Unit,
    ) {
        val updateLastCheckIn: suspend (lastCheckInPersistence: LastCheckInPersistence) -> Unit = { lastCheckIn ->
            checkInPreferencesStore.updateData { currentPreferences ->
                currentPreferences
                    .toBuilder()
                    .setCheckIn(
                        CheckIn
                            .newBuilder()
                            .setIsAnonymous(lastCheckIn.isAnonymous)
                            .setCurrentParkId(lastCheckIn.currentParkId)
                            .setTimestamp(lastCheckIn.timestamp)
                            .setPark(
                                Park
                                    .newBuilder()
                                    .setId(lastCheckIn.park.id)
                                    .setName(lastCheckIn.park.name)
                                    .setDescription(lastCheckIn.park.website),
                            ).build(),
                    ).build()
            }
        }

        val getLastCheckInPersistence: Flow<LastCheckInPersistence?> =
            checkInPreferencesStore.data
                .map {
                    if (it.checkIn.currentParkId.isNullOrBlank()) {
                        null
                    } else {
                        LastCheckInPersistence(
                            currentParkId = it.checkIn.currentParkId,
                            timestamp = it.checkIn.timestamp,
                            park = LastParkInPersistence(
                                id = it.checkIn.park.id,
                                name = it.checkIn.park.name,
                                website = it.checkIn.park.description,
                            ),
                            isAnonymous = it.checkIn.isAnonymous,
                        )
                    }
                }.catch { exception ->
                    if (exception is IOException) {
                        logError(exception)
                    } else {
                        throw exception
                    }
                }

        val removeLastCheckIn: suspend () -> Unit = {
            checkInPreferencesStore.updateData { checkIn ->
                checkIn.toBuilder().clear().build()
            }
        }
    }

data class LastCheckInPersistence(
    val isAnonymous: Boolean,
    val currentParkId: String,
    val timestamp: String,
    val park: LastParkInPersistence,
)

data class LastParkInPersistence(
    val id: String,
    val name: String,
    val website: String,
)
