package com.parkhang.mobile.core.userprofile.di

import com.parkhang.mobile.core.common.Dispatcher
import com.parkhang.mobile.core.common.PHDispatchers
import com.parkhang.mobile.core.userprofile.datasource.UserApi
import com.parkhang.mobile.core.userprofile.datasource.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class UserProfileModule {
    @Provides
    fun providesUserProfileRepository(
        @Dispatcher(PHDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        userApi: UserApi,
    ): UserProfileRepository =
        UserProfileRepository(
            ioDispatcher = ioDispatcher,
            fetchUserProfileInfo = {
                userApi.getMe()
            },
            updateUserProfileInfo = { userProfileInfo ->
                userApi.patchMe(userProfileInfo)
            },
        )
}
