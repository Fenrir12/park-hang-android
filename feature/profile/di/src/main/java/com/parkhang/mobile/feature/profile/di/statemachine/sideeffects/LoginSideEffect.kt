package com.parkhang.mobile.feature.profile.di.statemachine.sideeffects

import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import com.parkhang.mobile.feature.profile.di.statemachine.ProfileStateMachine
import com.parkhang.mobile.framework.authentication.entity.UserCredentials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginSideEffect(
    private val login: suspend (UserCredentials) -> Unit,
    private val getUserProfileInfo: suspend () -> UserProfileInfo,
    private val isUserLoggedIn: suspend () -> Boolean,
) : suspend (UserCredentials) -> Flow<ProfileStateMachine.UiState> {
    override suspend fun invoke(credentials: UserCredentials): Flow<ProfileStateMachine.UiState> = flow {
        try {
            login(credentials)
            val user = getUserProfileInfo()
            emit(ProfileStateMachine.UiState(isLoggedIn = isUserLoggedIn(), userProfileInfo = user))
        } catch (e: Exception) {
            emit(ProfileStateMachine.UiState(isLoggedIn = false, userProfileInfo = null))
        }
    }
}
