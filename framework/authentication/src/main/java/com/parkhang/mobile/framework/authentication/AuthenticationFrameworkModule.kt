package com.parkhang.mobile.framework.authentication

import android.util.Log
import com.parkhang.mobile.core.common.Dispatcher
import com.parkhang.mobile.core.common.PHDispatchers
import com.parkhang.mobile.framework.persistence.datasource.usercredentialspreferences.UserCredentialsDatasource
import com.parkhang.mobile.framework.persistence.datasource.usercredentialspreferences.UserProfile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationFrameworkModule {
    @Provides
    @Singleton
    fun providesAuthenticationFramework(
        @Dispatcher(PHDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        authenticationClient: AuthenticationClient,
        userCredentialsDatasource: UserCredentialsDatasource,
    ): AuthenticationFramework =
        AuthenticationFramework(
            ioDispatcher = ioDispatcher,
            onSignUp = { credentials ->
                Log.d("AuthenticationFramework", "Signing up with credentials: $credentials")
                authenticationClient
                    .signUp(credentials)
                    .mapCatching { payload ->
                        Log.d("AuthenticationFramework", "Sign up successful: $payload")
                        val authToken = payload.token.decodeJwt()
                        Log.v("AuthenticationFramework", "Decoded JWT: $authToken")
                        userCredentialsDatasource.updateUserCredentials(authToken)
                        authToken.user ?: throw IllegalStateException("User not found in token")
                    }.getOrElse { throwable ->
                        Log.e("AuthenticationFramework", "Sign up failed", throwable)
                        UserProfile.empty() // or a default user object on failure
                    }
            },
            onLogin = { credentials ->
                Log.d("AuthenticationFramework", "Logging in with credentials: $credentials")
                authenticationClient
                    .login(credentials)
                    .mapCatching { payload ->
                        Log.d("AuthenticationFramework", "Sign up successful: $payload")
                        val authToken = payload.token.decodeJwt()
                        Log.v("AuthenticationFramework", "Decoded JWT: $authToken")
                        userCredentialsDatasource.updateUserCredentials(authToken)
                        authToken.user ?: throw IllegalStateException("User not found in token")
                    }.getOrElse { throwable ->
                        Log.e("AuthenticationFramework", "Sign up failed", throwable)
                        UserProfile.empty() // or a default user object on failure
                    }
            },
            onLogout = {
                Log.d("AuthenticationFramework", "Logging out")
            },
            getIsUserLoggedIn = {
                Log.d("AuthenticationFramework", "Checking if user is logged in")
                userCredentialsDatasource.getUserAuthToken().first()?.let { token ->
                    Log.d("AuthenticationFramework", "Token found: $token")
                    val now = LocalDate.now(ZoneId.of("UTC"))
                    val expires =
                        token.expiresAt
                            .toInstant()
                            .atZone(ZoneId.of("UTC"))
                            .toLocalDate()
                    Log.v(
                        "AuthenticationFramework",
                        "Valid token? accessToken=${token.accessToken}, expires=$expires, now=$now, user=${token.user}",
                    )
                    token.accessToken.isNotEmpty() && expires.isAfter(now) && token.user != null
                } ?: run {
                    Log.d("AuthenticationFramework", "No user credentials found, user is not logged in")
                    false
                }
            },
        )
}
