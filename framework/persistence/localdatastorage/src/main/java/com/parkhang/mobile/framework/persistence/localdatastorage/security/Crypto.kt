package com.parkhang.mobile.framework.persistence.localdatastorage.security

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.crypto.Cipher

class Crypto(
    private val encryptCipher: suspend () -> Cipher,
    private val decryptCipher: suspend (byteArray: ByteArray) -> Cipher,
) {
    val encrypt: suspend (ByteArray, OutputStream) -> Unit = { rawBytes, outputStream ->
        val cipher = encryptCipher()
        val encryptedBytes = cipher.doFinal(rawBytes)

        DataOutputStream(outputStream).use { dataOut ->
            val iv = cipher.iv
            dataOut.writeInt(iv.size)
            dataOut.write(iv)
            dataOut.writeInt(encryptedBytes.size)
            dataOut.write(encryptedBytes)
        }
    }

    val decrypt: suspend (InputStream) -> ByteArray = { inputStream ->
        DataInputStream(inputStream).use { dataIn ->
            val ivSize = dataIn.readInt()
            val iv = ByteArray(ivSize).also { dataIn.readFully(it) }

            val encryptedSize = dataIn.readInt()
            val encrypted = ByteArray(encryptedSize).also { dataIn.readFully(it) }

            val cipher = decryptCipher(iv)
            cipher.doFinal(encrypted)
        }
    }
}
