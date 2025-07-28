package com.parkhang.mobile.feature.profile.di.statemachine

import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import com.parkhang.mobile.framework.authentication.entity.UserCredentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

class ProfileStateMachine(
    scope: CoroutineScope,
    private val signup: suspend (UserCredentials) -> Unit,
    private val login: suspend (UserCredentials) -> Unit,
    private val logout: suspend () -> Unit,
    private val isUserLoggedIn: suspend () -> Boolean,
    private val getUserProfileInfo: suspend () -> UserProfileInfo,
) {
    data class UiState(
        val isLoading: Boolean = false,
        val isLoggedIn: Boolean? = null,
        val username: String? = null,
        val error: String? = null,
    )

    sealed class UiIntent {
        data object CheckAuthentication : UiIntent()

        data class SignUp(
            val email: String,
            val password: String,
        ) : UiIntent()

        data class Login(
            val email: String,
            val password: String,
        ) : UiIntent()

        data object Logout : UiIntent()
    }

    private val _intents = MutableSharedFlow<UiIntent>(extraBufferCapacity = 1)

    val uiStateFlow: StateFlow<UiState>

    init {
        uiStateFlow =
            _intents
                .onStart { emit(UiIntent.CheckAuthentication) }
                .flatMapLatest { intent ->
                    when (intent) {
                        is UiIntent.CheckAuthentication -> {
                            checkAuthenticationFlow()
                        }

                        is UiIntent.SignUp -> {
                            signUpFlow(
                                credentials =
                                    UserCredentials(
                                        email = intent.email,
                                        password = intent.password,
                                    ),
                            )
                        }

                        is UiIntent.Login -> {
                            loginFlow(
                                credentials =
                                    UserCredentials(
                                        email = intent.email,
                                        password = intent.password,
                                    ),
                            )
                        }
                        is UiIntent.Logout -> {
                            logoutFlow()
                        }
                    }
                }.scan(UiState()) { previousState, newState ->
                    previousState.copy(
                        isLoggedIn = newState.isLoggedIn ?: previousState.isLoggedIn,
                        username = newState.username ?: previousState.username,
                    )
                }.stateIn(scope, SharingStarted.Eagerly, UiState())
    }

    fun processIntent(intent: UiIntent) {
        _intents.tryEmit(intent)
    }

    private fun checkAuthenticationFlow(): Flow<UiState> =
        flow {
            val isUserLoggedIn = isUserLoggedIn()
            if (isUserLoggedIn) {
                val userProfileInfo = getUserProfileInfo()
                emit(
                    UiState(
                        isLoggedIn = true,
                        username = userProfileInfo.email,
                    ),
                )
            } else {
                emit(UiState(isLoggedIn = false, username = null))
            }
        }

    private fun signUpFlow(credentials: UserCredentials): Flow<UiState> =
        flow {
            try {
                signup(credentials)
                emit(UiState(isLoggedIn = isUserLoggedIn(), username = credentials.email))
            } catch (e: Exception) {
                emit(UiState(isLoggedIn = false, username = null))
            }
        }

    private fun loginFlow(credentials: UserCredentials): Flow<UiState> =
        flow {
            try {
                login(credentials)
                emit(UiState(isLoggedIn = isUserLoggedIn(), username = credentials.email))
            } catch (e: Exception) {
                emit(UiState(isLoggedIn = false, username = null))
            }
        }

    private fun logoutFlow(): Flow<UiState> =
        flow {
            try {
                logout()
                emit(UiState(isLoggedIn = false, username = null))
            } catch (e: Exception) {
                emit(UiState(error = "Logout failed: ${e.message}"))
            }
        }
}
