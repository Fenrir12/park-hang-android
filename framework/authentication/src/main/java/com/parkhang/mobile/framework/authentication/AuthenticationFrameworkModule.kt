package com.parkhang.mobile.framework.authentication

import android.util.Log
import com.parkhang.mobile.core.common.Dispatcher
import com.parkhang.mobile.core.common.PHDispatchers
import com.parkhang.mobile.core.event.AppEventBus
import com.parkhang.mobile.core.event.EventModule.AppEvent
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
        appEventBus: AppEventBus,
    ): AuthenticationFramework = AuthenticationFramework(
        ioDispatcher = ioDispatcher,
        onSignUp = { credentials ->
            authenticationClient
                .signUp(credentials)
                .mapCatching { payload ->
                    Log.d("AuthenticationFramework", "Sign up successful")
                    val authToken = payload.token.decodeJwt()
                    userCredentialsDatasource.updateUserCredentials(authToken)
                    authToken.user ?: throw IllegalStateException("User not found in token")
                }.getOrElse { throwable ->
                    Log.e("AuthenticationFramework", "Sign up failed", throwable)
                    UserProfile.empty() // or a default user object on failure
                }
        },
        onLogin = { credentials ->
            authenticationClient
                .login(credentials)
                .mapCatching { payload ->
                    Log.d("AuthenticationFramework", "Log in successful")
                    val authToken = payload.token.decodeJwt()
                    userCredentialsDatasource.updateUserCredentials(authToken)
                    authToken.user ?: throw IllegalStateException("User not found in token")
                }.getOrElse { throwable ->
                    Log.e("AuthenticationFramework", "Sign up failed", throwable)
                    UserProfile.empty() // or a default user object on failure
                }
        },
        onLogout = {
            Log.d("AuthenticationFramework", "Logging out")
            userCredentialsDatasource.deleteUserCredentials()
            appEventBus.emit(AppEvent.UserLoggedOut)
        },
        getIsUserLoggedIn = {
            userCredentialsDatasource.getUserAuthToken().first()?.let { token ->
                val now = LocalDate.now(ZoneId.of("UTC"))
                val expires =
                    token.expiresAt
                        .toInstant()
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate()
                token.accessToken.isNotEmpty() && expires.isAfter(now) && token.user != null
            } ?: run {
                Log.d("AuthenticationFramework", "No user credentials found, user is not logged in")
                false
            }
        },
    )
}
