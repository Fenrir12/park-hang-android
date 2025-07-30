package com.parkhang.mobile.feature.profile.di.statemachine.sideeffects

import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import com.parkhang.mobile.feature.profile.di.statemachine.ProfileStateMachine
import com.parkhang.mobile.feature.profile.di.statemachine.SignUpFormErrorCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ValidateSignUpFormSideEffect : suspend (UserProfileInfo, String, String) -> Flow<ProfileStateMachine.UiState> {
    override suspend fun invoke(
        newUserFormInfo: UserProfileInfo,
        password: String,
        confirmPassword: String,
    ): Flow<ProfileStateMachine.UiState> = flow {
        when {
            !android.util.Patterns.EMAIL_ADDRESS
                .matcher(newUserFormInfo.email)
                .matches() -> {
                emit(ProfileStateMachine.UiState(error = "Invalid email format", formErrorCode = SignUpFormErrorCode.InvalidEmail))
            }
            password != confirmPassword -> {
                emit(ProfileStateMachine.UiState(error = "Passwords do not match", formErrorCode = SignUpFormErrorCode.PasswordMismatch))
            }
            else -> {
                emit(ProfileStateMachine.UiState(isLoggedIn = false, userProfileInfo = null, isSignUpFormValid = true, formErrorCode = null))
            }
        }
    }
}
