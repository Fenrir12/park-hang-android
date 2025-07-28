package com.parkhang.mobile.feature.profile.di

import com.parkhang.mobile.core.common.Dispatcher
import com.parkhang.mobile.core.common.PHDispatchers
import com.parkhang.mobile.feature.profile.di.statemachine.ProfileStateMachine
import com.parkhang.mobile.framework.authentication.AuthenticationFramework
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProfileModule {
    @Provides
    @Singleton
    fun providesProfileStateMachine(
        @Dispatcher(PHDispatchers.DEFAULT) dispatcher: CoroutineDispatcher,
        authenticationFramework: AuthenticationFramework,
    ) = ProfileStateMachine(
        scope = CoroutineScope(dispatcher),
        signup = { credentials ->
            authenticationFramework.signUp(credentials)
        },
        login = { credentials ->
            authenticationFramework.login(credentials)
        },
        isUserLoggedIn = {
            authenticationFramework.isUserLoggedIn()
        },
    )
}
