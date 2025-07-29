package com.parkhang.mobile.framework.persistence.localdatastorage

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.parkhang.mobile.framework.persistence.usercredentialsprefs.UserCredentialsPreferences
import java.io.InputStream
import java.io.OutputStream

/**
 * Serializer for the [UserCredentialsPreferences] object defined in user_events_prefs.proto.
 */
class UserCredentialsSerializer(
    val encrypt: suspend (ByteArray, OutputStream) -> Unit,
    val decrypt: suspend (InputStream) -> ByteArray,
) : Serializer<UserCredentialsPreferences> {
    override val defaultValue: UserCredentialsPreferences = UserCredentialsPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserCredentialsPreferences = try {
        UserCredentialsPreferences.parseFrom(decrypt(input))
    } catch (exception: Exception) {
        throw CorruptionException("Cannot read proto.", exception)
    }

    override suspend fun writeTo(
        t: UserCredentialsPreferences,
        output: OutputStream,
    ) {
        encrypt(t.toByteArray(), output)
        output.close()
    }
}
