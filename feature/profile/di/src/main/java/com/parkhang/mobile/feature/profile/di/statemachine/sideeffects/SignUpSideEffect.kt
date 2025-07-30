package com.parkhang.mobile.feature.profile.di.statemachine.sideeffects

import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import com.parkhang.mobile.feature.profile.di.statemachine.ProfileStateMachine
import com.parkhang.mobile.framework.authentication.entity.UserCredentials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignUpSideEffect(
    private val signup: suspend (UserCredentials) -> Unit,
    private val updateUserProfile: suspend (UserProfileInfo) -> UserProfileInfo,
    private val isUserLoggedIn: suspend () -> Boolean,
) : suspend (UserCredentials, UserProfileInfo) -> Flow<ProfileStateMachine.UiState> {
    override suspend fun invoke(
        credentials: UserCredentials,
        newUserFormInfo: UserProfileInfo,
    ): Flow<ProfileStateMachine.UiState> = flow {
        try {
            signup(credentials)
            val user = updateUserProfile(newUserFormInfo)
            emit(ProfileStateMachine.UiState(isLoggedIn = isUserLoggedIn(), userProfileInfo = user))
        } catch (e: Exception) {
            emit(ProfileStateMachine.UiState(isLoggedIn = false, userProfileInfo = null))
        }
    }
}
