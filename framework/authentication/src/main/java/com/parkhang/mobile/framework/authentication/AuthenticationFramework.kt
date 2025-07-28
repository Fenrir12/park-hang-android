package com.parkhang.mobile.framework.authentication

import com.parkhang.mobile.framework.authentication.entity.UserCredentials
import com.parkhang.mobile.framework.persistence.datasource.usercredentialspreferences.UserProfile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AuthenticationFramework(
    private val ioDispatcher: CoroutineDispatcher,
    private val onSignUp: suspend (UserCredentials) -> UserProfile?,
    private val onLogin: suspend (UserCredentials) -> UserProfile?,
    private val onLogout: suspend () -> Unit,
    private val getIsUserLoggedIn: suspend () -> Boolean,
) {
    val signUp: suspend (UserCredentials) -> Unit = { credentials ->
        withContext(ioDispatcher) {
            onSignUp(credentials)
        }
    }
    val login: suspend (UserCredentials) -> Unit = { credentials ->
        withContext(ioDispatcher) {
            onLogin(credentials)
        }
    }
    val logout: suspend () -> Unit = {
        withContext(ioDispatcher) {
            onLogout()
        }
    }

    val isUserLoggedIn: suspend () -> Boolean = {
        withContext(ioDispatcher) {
            getIsUserLoggedIn()
        }
    }
}
