package com.parkhang.mobile.feature.profile.di.statemachine.sideeffects

import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import com.parkhang.mobile.feature.profile.di.statemachine.ProfileStateMachine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CheckAuthenticationSideEffect(
    private val isUserLoggedIn: suspend () -> Boolean,
    private val getUserProfileInfo: suspend () -> UserProfileInfo,
) : suspend () -> Flow<ProfileStateMachine.UiState> {
    override suspend fun invoke(): Flow<ProfileStateMachine.UiState> = flow {
        val loggedIn = isUserLoggedIn()
        if (loggedIn) {
            val profile = getUserProfileInfo()
            emit(ProfileStateMachine.UiState(isLoggedIn = true, userProfileInfo = profile))
        } else {
            emit(ProfileStateMachine.UiState(isLoggedIn = false, userProfileInfo = null))
        }
    }
}
