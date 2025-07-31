package com.parkhang.mobile.feature.profile.di.statemachine

import com.parkhang.mobile.core.common.logStateTransitionsPretty
import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import com.parkhang.mobile.feature.profile.di.statemachine.sideeffects.CheckAuthenticationSideEffect
import com.parkhang.mobile.feature.profile.di.statemachine.sideeffects.LoginSideEffect
import com.parkhang.mobile.feature.profile.di.statemachine.sideeffects.LogoutSideEffect
import com.parkhang.mobile.feature.profile.di.statemachine.sideeffects.SignUpSideEffect
import com.parkhang.mobile.feature.profile.di.statemachine.sideeffects.ValidatePasswordSideEffect
import com.parkhang.mobile.feature.profile.di.statemachine.sideeffects.ValidateSignUpFormSideEffect
import com.parkhang.mobile.framework.authentication.entity.UserCredentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

class ProfileStateMachine(
    scope: CoroutineScope,
    private val checkAuthenticationSideEffect: CheckAuthenticationSideEffect,
    private val validatePasswordSideEffect: ValidatePasswordSideEffect,
    private val validateSignUpFormSideEffect: ValidateSignUpFormSideEffect,
    private val signUpSideEffect: SignUpSideEffect,
    private val loginSideEffect: LoginSideEffect,
    private val logoutSideEffect: LogoutSideEffect,
) {
    data class UiState(
        val isLoading: Boolean = false,
        val isLoggedIn: Boolean? = null,
        val userProfileInfo: UserProfileInfo? = null,
        val isSignUpFormValid: Boolean = false,
        val error: String? = null,
        val formErrorCode: SignUpFormErrorCode? = null,
        val passwordErrorCodeList: List<PasswordFormErrorCode> = listOf(
            PasswordFormErrorCode.TooShort,
            PasswordFormErrorCode.MissingSymbol,
            PasswordFormErrorCode.MissingCapitalCharacter,
            PasswordFormErrorCode.MissingNumber,
        ),
    )

    sealed class UiIntent {
        object CheckAuthentication : UiIntent()

        data class ValidatePassword(
            val password: String,
        ) : UiIntent()

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

        object Logout : UiIntent()
    }

    private val intents = MutableSharedFlow<UiIntent>(extraBufferCapacity = 1)

    fun processIntent(intent: UiIntent) {
        intents.tryEmit(intent)
    }

    val uiStateFlow: StateFlow<UiState> = intents
        .onStart { emit(UiIntent.CheckAuthentication) }
        .flatMapLatest { intent ->
            when (intent) {
                is UiIntent.CheckAuthentication -> checkAuthenticationSideEffect()
                is UiIntent.ValidatePassword -> validatePasswordSideEffect(intent.password)
                is UiIntent.ValidateSignUpForm -> {
                    validateSignUpFormSideEffect
                        .invoke(
                            intent.newUserFormInfo,
                            intent.password,
                            intent.confirmPassword,
                        ).onEach { newState ->
                            if (newState.isSignUpFormValid) {
                                processIntent(UiIntent.SignUp(intent.newUserFormInfo, intent.password))
                            }
                        }
                }

                is UiIntent.SignUp -> signUpSideEffect(
                    UserCredentials(
                        email = intent.newUserFormInfo.email,
                        password = intent.password,
                    ),
                    intent.newUserFormInfo,
                )

                is UiIntent.Login -> loginSideEffect(
                    UserCredentials(
                        email = intent.email,
                        password = intent.password,
                    ),
                )

                is UiIntent.Logout -> logoutSideEffect()
            }
        }.scan(UiState()) { previousState, newState ->
            previousState.copy(
                isLoggedIn = newState.isLoggedIn ?: previousState.isLoggedIn,
                userProfileInfo = newState.userProfileInfo ?: previousState.userProfileInfo,
                isSignUpFormValid = newState.isSignUpFormValid || previousState.isSignUpFormValid,
                error = newState.error ?: previousState.error,
                formErrorCode = newState.formErrorCode ?: previousState.formErrorCode,
                passwordErrorCodeList =
                    if (newState.passwordErrorCodeList.isNotEmpty()) newState.passwordErrorCodeList else previousState.passwordErrorCodeList,
            )
        }.logStateTransitionsPretty("ProfileState")
        .stateIn(scope, SharingStarted.Eagerly, UiState())
}

sealed class PasswordFormErrorCode {
    object TooShort : PasswordFormErrorCode()

    object MissingSymbol : PasswordFormErrorCode()

    object MissingCapitalCharacter : PasswordFormErrorCode()

    object MissingNumber : PasswordFormErrorCode()
}

sealed class SignUpFormErrorCode {
    object InvalidEmail : SignUpFormErrorCode()

    object PasswordMismatch : SignUpFormErrorCode()
}
