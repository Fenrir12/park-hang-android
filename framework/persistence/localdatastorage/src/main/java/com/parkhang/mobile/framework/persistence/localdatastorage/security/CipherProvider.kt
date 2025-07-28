package com.parkhang.mobile.framework.persistence.localdatastorage.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

class CipherProvider
    @Inject
    constructor(
        private val keyStore: KeyStore,
        private val keyName: String,
        private val keyStoreName: String,
    ) {
        private val keyLock = Mutex()

        val encryptCipher: suspend () -> Cipher = {
            Cipher.getInstance(TRANSFORMATION).apply {
                init(Cipher.ENCRYPT_MODE, getOrCreateKey())
            }
        }

        val decryptCipher: suspend (ByteArray) -> Cipher = { byteArray ->
            Cipher.getInstance(TRANSFORMATION).apply {
                init(Cipher.DECRYPT_MODE, getOrCreateKey(), IvParameterSpec(byteArray))
            }
        }

        private suspend fun getOrCreateKey(): SecretKey =
            keyLock.withLock {
                (keyStore.getEntry(keyName, null) as? KeyStore.SecretKeyEntry)?.secretKey ?: generateKey()
            }

        private fun generateKey(): SecretKey =
            KeyGenerator
                .getInstance(ALGORITHM, keyStoreName)
                .apply { init(keyGenParams) }
                .generateKey()

        private val keyGenParams =
            KeyGenParameterSpec
                .Builder(
                    keyName,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                ).apply {
                    setBlockModes(BLOCK_MODE)
                    setEncryptionPaddings(PADDING)
                    setUserAuthenticationRequired(false)
                    setRandomizedEncryptionRequired(true)
                }.build()
    }

private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
