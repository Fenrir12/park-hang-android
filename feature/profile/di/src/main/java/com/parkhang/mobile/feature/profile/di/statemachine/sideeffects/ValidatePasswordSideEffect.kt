package com.parkhang.mobile.feature.profile.di.statemachine.sideeffects

import com.parkhang.mobile.feature.profile.di.statemachine.PasswordFormErrorCode
import com.parkhang.mobile.feature.profile.di.statemachine.ProfileStateMachine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ValidatePasswordSideEffect : suspend (String) -> Flow<ProfileStateMachine.UiState> {
    override suspend fun invoke(password: String): Flow<ProfileStateMachine.UiState> = flow {
        val errors = mutableListOf<PasswordFormErrorCode>()

        if (password.length < MINIMUM_PASSWORD_SIZE) errors.add(PasswordFormErrorCode.TooShort)
        if (!password.any { !it.isLetterOrDigit() }) errors.add(PasswordFormErrorCode.MissingSymbol)
        if (!password.any { it.isUpperCase() }) errors.add(PasswordFormErrorCode.MissingCapitalCharacter)
        if (!password.any { it.isDigit() }) errors.add(PasswordFormErrorCode.MissingNumber)

        if (errors.size > 1) {
            emit(ProfileStateMachine.UiState(error = "Password must meet at least 3 of 4 rules", passwordErrorCodeList = errors))
        } else {
            emit(ProfileStateMachine.UiState(error = null, passwordErrorCodeList = emptyList()))
        }
    }
}

private const val MINIMUM_PASSWORD_SIZE = 6
