package com.parkhang.mobile.framework.persistence.localdatastorage

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * Serializer for the [CheckInPreferences] object defined in checkin_prefs.proto.
 */
class CheckInPreferencesSerializer
    @Inject
    constructor() : Serializer<CheckInPreferences> {
        override val defaultValue: CheckInPreferences = CheckInPreferences.getDefaultInstance()

        override suspend fun readFrom(input: InputStream): CheckInPreferences = try {
            CheckInPreferences.parseFrom(input)
        } catch (exception: Exception) {
            throw CorruptionException("Cannot read protocol.", exception)
        }

        override suspend fun writeTo(
            t: CheckInPreferences,
            output: OutputStream,
        ) {
            t.writeTo(output)
        }
    }
