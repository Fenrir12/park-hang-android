package com.parkhang.mobile.framework.persistence.datasource.usercredentialspreferences

import androidx.datastore.core.DataStore
import com.parkhang.mobile.framework.persistence.usercredentialsprefs.UserCredentialsPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserCredentialsDataSourceModule {
    @Provides
    fun providesUserCredentialsDataSource(
        userCredentialsPreferencesStore: DataStore<UserCredentialsPreferences>,
    ): UserCredentialsDatasource =
        UserCredentialsDatasource(
            userCredentialsPreferencesStore = userCredentialsPreferencesStore,
        )
}
