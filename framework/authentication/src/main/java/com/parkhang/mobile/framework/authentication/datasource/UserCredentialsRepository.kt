package com.parkhang.mobile.framework.authentication.datasource

import com.parkhang.mobile.framework.authentication.entity.UserCredentials
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UserCredentialsRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val removeUserCredentials: suspend () -> Unit,
    private val storeUserCredentials: suspend (UserCredentials) -> Unit,
    private val requestUserCredentials: suspend () -> UserCredentials,
) {
    val getUserCredentials: suspend () -> UserCredentials = {
        withContext(ioDispatcher) {
            requestUserCredentials()
        }
    }

    val saveUserCredentials: suspend (UserCredentials) -> Unit = { userCredentials ->
        withContext(ioDispatcher) {
            storeUserCredentials(userCredentials)
        }
    }

    val deleteUserCredentials: suspend () -> Unit = {
        withContext(ioDispatcher) {
            removeUserCredentials()
        }
    }
}
