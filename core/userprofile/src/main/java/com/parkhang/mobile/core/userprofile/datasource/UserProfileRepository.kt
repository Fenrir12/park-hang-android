package com.parkhang.mobile.core.userprofile.datasource

import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UserProfileRepository(
    ioDispatcher: CoroutineDispatcher,
    fetchUserProfileInfo: suspend () -> Result<UserProfileInfo>,
    updateUserProfileInfo: suspend (UserProfileInfo) -> Result<UserProfileInfo>,
) {
    val getUserProfileInfo: suspend () -> UserProfileInfo = {
        withContext(ioDispatcher) {
            fetchUserProfileInfo().getOrThrow()
        }
    }

    val patchUserProfileInfo: suspend (UserProfileInfo) -> UserProfileInfo = { userProfileInfo ->
        withContext(ioDispatcher) {
            updateUserProfileInfo(userProfileInfo).getOrThrow()
        }
    }
}
