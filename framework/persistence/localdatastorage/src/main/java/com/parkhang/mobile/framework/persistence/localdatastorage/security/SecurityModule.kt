package com.parkhang.mobile.framework.persistence.localdatastorage.security

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.security.KeyStore

@Module
@InstallIn(SingletonComponent::class)
internal object SecurityModule {
    @Provides
    fun providesCipherProvider(): CipherProvider =
        CipherProvider(
            keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_TYPE).apply { load(null) },
            keyName = KEY_STORE_ENTRY_ALIAS,
            keyStoreName = ANDROID_KEY_STORE_TYPE,
        )

    @Provides
    fun providesCrypto(cipherProvider: CipherProvider): Crypto =
        Crypto(
            encryptCipher = cipherProvider.encryptCipher,
            decryptCipher = cipherProvider.decryptCipher,
        )
}

internal const val ANDROID_KEY_STORE_TYPE = "AndroidKeyStore"
internal const val KEY_STORE_ENTRY_ALIAS = "TtDataStoreKey_v2"
