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
    private val updateUserProfile: suspend (UserProfileInfo) -> UserProfileInfo,
    private val login: suspend (UserCredentials) -> Unit,
    private val logout: suspend () -> Unit,
    private val isUserLoggedIn: suspend () -> Boolean,
    private val getUserProfileInfo: suspend () -> UserProfileInfo,
) {
    data class UiState(
        val isLoading: Boolean = false,
        val isLoggedIn: Boolean? = null,
        val userProfileInfo: UserProfileInfo? = null,
        val isSignUpFormValid: Boolean = false,
        val error: String? = null,
        val formErrorCode: SignUpFormErrorCode? = null,
    )

    sealed class UiIntent {
        data object CheckAuthentication : UiIntent()

        data class ValidateSignUpForm(
            val newUserFormInfo: UserProfileInfo,
            val password: String,
            val confirmPassword: String,
        ) : UiIntent()

        data class SignUp(
            val newUserFormInfo: UserProfileInfo,
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

                        is UiIntent.ValidateSignUpForm -> {
                            validateSigUpFormFlow(
                                newUserFormInfo = intent.newUserFormInfo,
                                password = intent.password,
                                confirmPassword = intent.confirmPassword,
                            )
                        }

                        is UiIntent.SignUp -> {
                            signUpFlow(
                                credentials =
                                    UserCredentials(
                                        email = intent.newUserFormInfo.email,
                                        password = intent.password,
                                    ),
                                newUserFormInfo = intent.newUserFormInfo,
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
                        userProfileInfo = newState.userProfileInfo ?: previousState.userProfileInfo,
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
                        userProfileInfo = userProfileInfo,
                    ),
                )
            } else {
                emit(UiState(isLoggedIn = false, userProfileInfo = null))
            }
        }

    private fun validateSigUpFormFlow(
        newUserFormInfo: UserProfileInfo,
        password: String,
        confirmPassword: String,
    ): Flow<UiState> =
        flow {
            when {
                newUserFormInfo.email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                    emit(
                        UiState(
                            error = "Email and password fields cannot be empty",
                            formErrorCode = SignUpFormErrorCode.UnknownError,
                        ),
                    )
                }

                !android.util.Patterns.EMAIL_ADDRESS
                    .matcher(newUserFormInfo.email)
                    .matches() -> {
                    emit(
                        UiState(
                            error = "Invalid email format",
                            formErrorCode = SignUpFormErrorCode.InvalidEmail,
                        ),
                    )
                }

                password.length < 6 -> {
                    emit(
                        UiState(
                            error = "Password must be at least 6 characters",
                            formErrorCode = SignUpFormErrorCode.PasswordTooShort,
                        ),
                    )
                }

                password != confirmPassword -> {
                    emit(
                        UiState(
                            error = "Passwords do not match",
                            formErrorCode = SignUpFormErrorCode.PasswordMismatch,
                        ),
                    )
                }

                else -> {
                    emit(
                        UiState(
                            isLoggedIn = false,
                            userProfileInfo = null,
                            isSignUpFormValid = true,
                            formErrorCode = null,
                        ),
                    )
                    processIntent(
                        UiIntent.SignUp(
                            newUserFormInfo = newUserFormInfo,
                            password = password,
                        ),
                    )
                }
            }
        }

    private fun signUpFlow(
        credentials: UserCredentials,
        newUserFormInfo: UserProfileInfo,
    ): Flow<UiState> =
        flow {
            try {
                signup(credentials)
                val user = updateUserProfile(newUserFormInfo)
                emit(UiState(isLoggedIn = isUserLoggedIn(), userProfileInfo = user))
            } catch (e: Exception) {
                emit(UiState(isLoggedIn = false, userProfileInfo = null))
            }
        }

    private fun loginFlow(credentials: UserCredentials): Flow<UiState> =
        flow {
            try {
                login(credentials)
                val user = getUserProfileInfo()
                emit(UiState(isLoggedIn = isUserLoggedIn(), userProfileInfo = user))
            } catch (e: Exception) {
                emit(UiState(isLoggedIn = false, userProfileInfo = null))
            }
        }

    private fun logoutFlow(): Flow<UiState> =
        flow {
            try {
                logout()
                emit(UiState(isLoggedIn = false, userProfileInfo = null))
            } catch (e: Exception) {
                emit(UiState(error = "Logout failed: ${e.message}"))
            }
        }
}

sealed class SignUpFormErrorCode {
    object NONE : SignUpFormErrorCode()

    object InvalidEmail : SignUpFormErrorCode()

    object PasswordTooShort : SignUpFormErrorCode()

    object PasswordMismatch : SignUpFormErrorCode()

    object UnknownError : SignUpFormErrorCode()
}
