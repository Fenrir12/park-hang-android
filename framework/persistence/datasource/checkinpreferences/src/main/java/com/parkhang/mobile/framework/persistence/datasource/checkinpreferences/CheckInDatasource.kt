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
        val updateLastCheckIn: suspend (lastCheckIn: LastCheckIn) -> Unit = { lastCheckIn ->
            checkInPreferencesStore.updateData { currentPreferences ->
                currentPreferences
                    .toBuilder()
                    .setCheckIn(
                        CheckIn
                            .newBuilder()
                            .setCurrentParkId(lastCheckIn.currentParkId)
                            .setTimestamp(lastCheckIn.timestamp)
                            .setPark(
                                Park
                                    .newBuilder()
                                    .setId(lastCheckIn.place.id)
                                    .setName(lastCheckIn.place.name)
                                    .setDescription(lastCheckIn.place.description),
                            )
                            .build(),
                    ).build()
            }
        }

        val getLastCheckIn: Flow<LastCheckIn?> =
            checkInPreferencesStore.data
                .map {
                    if (it.checkIn.currentParkId.isNullOrBlank()) {
                        null
                    } else {
                        LastCheckIn(
                            it.checkIn.currentParkId,
                            it.checkIn.timestamp,
                            LastPark(
                                id = it.checkIn.park.id,
                                name = it.checkIn.park.name,
                                description = it.checkIn.park.description,
                            ),
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

        val getCurrentParkId: Flow<String> =
            checkInPreferencesStore.data
                .map {
                    it.checkIn.currentParkId
                }.catch { exception ->
                    if (exception is IOException) {
                        logError(exception)
                        emit(CheckInPreferences.getDefaultInstance().checkIn.currentParkId)
                    } else {
                        throw exception
                    }
                }

        val getParkName: Flow<String> =
            checkInPreferencesStore.data
                .map {
                    it.checkIn.park.name
                }.catch { exception ->
                    if (exception is IOException) {
                        logError(exception)
                        emit(
                            CheckInPreferences
                                .getDefaultInstance()
                                .checkIn.park.name,
                        )
                    } else {
                        throw exception
                    }
                }
    }

data class LastCheckIn(
    val currentParkId: String,
    val timestamp: String,
    val place: LastPark,
)

data class LastPark(
    val id: String,
    val name: String,
    val description: String = "",
)
