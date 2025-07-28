package com.parkhang.mobile.core.userprofile.datasource

import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UserProfileRepository(
    ioDispatcher: CoroutineDispatcher,
    fetchUserProfileInfo: suspend () -> Result<UserProfileInfo>,
) {
    val getUserProfileInfo: suspend () -> UserProfileInfo? = {
        withContext(ioDispatcher) {
            fetchUserProfileInfo().getOrNull()
        }
    }
}
