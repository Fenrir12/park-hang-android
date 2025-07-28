package com.parkhang.mobile.framework.persistence.datasource.usercredentialspreferences

import android.util.Log
import androidx.datastore.core.DataStore
import com.parkhang.mobile.core.common.orZero
import com.parkhang.mobile.framework.persistence.usercredentialsprefs.UserCredentialsPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.Date
import javax.inject.Inject

class UserCredentialsDatasource
    @Inject
    constructor(
        private val userCredentialsPreferencesStore: DataStore<UserCredentialsPreferences>,
    ) {
        val getUserAuthToken: suspend () -> Flow<UserAuthToken?> = {
            userCredentialsPreferencesStore.data
                .map { data ->
                    with(data) {
                        UserAuthToken(
                            idToken = idToken,
                            accessToken = accessToken,
                            expiresAt = Date(expiresAtTimestamp),
                            user =
                                UserProfile(
                                    id = user.id,
                                    name = user.name,
                                    nickname = user.nickname,
                                    email = user.email,
                                    familyName = user.familyName,
                                    createdAt = Date(user.createdAt),
                                ),
                        )
                    }
                }.catch { exception ->
                    if (exception is IOException) {
                        emit(UserAuthToken.empty())
                    } else {
                        throw exception
                    }
                }
        }

        val updateUserCredentials: suspend (userAuthToken: UserAuthToken) -> Unit = { userCredentials ->
            try {
                userCredentialsPreferencesStore
                    .updateData { currentPreferences ->
                        currentPreferences
                            .toBuilder()
                            .setIdToken(userCredentials.idToken)
                            .setAccessToken(userCredentials.accessToken)
                            .setExpiresAtTimestamp(userCredentials.expiresAt.time)
                            .setUser(
                                currentPreferences.user
                                    .toBuilder()
                                    .setId(userCredentials.user?.id.orEmpty())
                                    .setName(userCredentials.user?.name.orEmpty())
                                    .setNickname(userCredentials.user?.nickname.orEmpty())
                                    .setEmail(userCredentials.user?.email.orEmpty())
                                    .setFamilyName(userCredentials.user?.familyName.orEmpty())
                                    .setCreatedAt(
                                        userCredentials.user
                                            ?.createdAt
                                            ?.time
                                            .orZero(),
                                    ).build(),
                            ).build()
                    }
            } catch (exception: IOException) {
                Log.e("UserCredentialsDatasource", "Error updating user credentials", exception)
            } catch (exception: Exception) {
                Log.e("UserCredentialsDatasource", "Unexpected error updating user credentials", exception)
                throw exception
            }
        }

        val deleteUserCredentials: suspend () -> Unit = {
            userCredentialsPreferencesStore.updateData { UserCredentialsPreferences.getDefaultInstance() }
        }
    }

data class UserAuthToken(
    val idToken: String,
    val accessToken: String,
    val expiresAt: Date,
    val user: UserProfile? = null,
) {
    companion object {
        fun empty() =
            UserAuthToken(
                idToken = "",
                accessToken = "",
                expiresAt = Date(0),
            )
    }
}

data class UserProfile(
    val id: String,
    val name: String? = null,
    val nickname: String? = null,
    val email: String,
    val familyName: String? = null,
    val createdAt: Date? = null,
) {
    companion object {
        fun empty() =
            UserProfile(
                id = "",
                name = null,
                nickname = null,
                email = "",
                familyName = null,
                createdAt = null,
            )
    }
}
