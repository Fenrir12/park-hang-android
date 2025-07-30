package com.parkhang.mobile.framework.persistence.datasource.checkinpreferences

import androidx.datastore.core.DataStore
import com.parkhang.mobile.framework.persistence.localdatastorage.CheckInPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CheckInDataSourceModule {
    @Provides
    fun providesCheckInDatasource(checkInPreferencesStore: DataStore<CheckInPreferences>): CheckInDatasource = CheckInDatasource(
        checkInPreferencesStore = checkInPreferencesStore,
        logError = { }, // TODO: Add error logging to crashlytics
    )
}
