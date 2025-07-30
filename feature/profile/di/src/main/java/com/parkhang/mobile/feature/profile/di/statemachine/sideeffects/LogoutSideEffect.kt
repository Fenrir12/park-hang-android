package com.parkhang.mobile.feature.profile.di.statemachine.sideeffects

import com.parkhang.mobile.feature.profile.di.statemachine.ProfileStateMachine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LogoutSideEffect(
    private val logout: suspend () -> Unit,
) : suspend () -> Flow<ProfileStateMachine.UiState> {
    override suspend fun invoke(): Flow<ProfileStateMachine.UiState> = flow {
        try {
            logout()
            emit(ProfileStateMachine.UiState(isLoggedIn = false, userProfileInfo = null))
        } catch (e: Exception) {
            emit(ProfileStateMachine.UiState(error = "Logout failed: ${e.message}"))
        }
    }
}
