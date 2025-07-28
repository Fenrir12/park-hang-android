package com.parkhang.mobile.framework.persistence.localdatastorage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.parkhang.mobile.core.common.Dispatcher
import com.parkhang.mobile.core.common.PHDispatchers
import com.parkhang.mobile.framework.persistence.localdatastorage.security.Crypto
import com.parkhang.mobile.framework.persistence.usercredentialsprefs.UserCredentialsPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LocalDataStorageModule {
    @Provides
    @Singleton
    fun providesUserCredentialsSerializer(crypto: Crypto): UserCredentialsSerializer =
        UserCredentialsSerializer(
            encrypt = crypto.encrypt,
            decrypt = crypto.decrypt,
        )

    @Provides
    @Singleton
    fun providesUserCredentialsPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(PHDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        userCredentialsSerializer: UserCredentialsSerializer,
    ): DataStore<UserCredentialsPreferences> =
        DataStoreFactory.create(
            serializer = userCredentialsSerializer,
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
            corruptionHandler =
                ReplaceFileCorruptionHandler {
                    UserCredentialsPreferences.getDefaultInstance()
                },
        ) {
            context.dataStoreFile(USER_CREDENTIALS_PREFS_FILE)
        }
}

const val USER_CREDENTIALS_PREFS_FILE = "user_credentials_prefs.pb"
