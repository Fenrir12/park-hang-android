package com.parkhang.mobile.feature.profile.di

import com.parkhang.mobile.core.common.Dispatcher
import com.parkhang.mobile.core.common.PHDispatchers
import com.parkhang.mobile.core.userprofile.datasource.UserProfileRepository
import com.parkhang.mobile.feature.profile.di.statemachine.ProfileStateMachine
import com.parkhang.mobile.feature.profile.di.statemachine.sideeffects.CheckAuthenticationSideEffect
import com.parkhang.mobile.feature.profile.di.statemachine.sideeffects.LoginSideEffect
import com.parkhang.mobile.feature.profile.di.statemachine.sideeffects.LogoutSideEffect
import com.parkhang.mobile.feature.profile.di.statemachine.sideeffects.SignUpSideEffect
import com.parkhang.mobile.feature.profile.di.statemachine.sideeffects.ValidatePasswordSideEffect
import com.parkhang.mobile.feature.profile.di.statemachine.sideeffects.ValidateSignUpFormSideEffect
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
    fun provideCheckAuthenticationSideEffect(
        authenticationFramework: AuthenticationFramework,
        userProfileRepository: UserProfileRepository,
    ): CheckAuthenticationSideEffect = CheckAuthenticationSideEffect(
        isUserLoggedIn = authenticationFramework.isUserLoggedIn,
        getUserProfileInfo = userProfileRepository.getUserProfileInfo,
    )

    @Provides
    @Singleton
    fun provideValidatePasswordSideEffect(): ValidatePasswordSideEffect = ValidatePasswordSideEffect()

    @Provides
    @Singleton
    fun provideValidateSignUpFormSideEffect(): ValidateSignUpFormSideEffect = ValidateSignUpFormSideEffect()

    @Provides
    @Singleton
    fun provideSignUpSideEffect(
        authenticationFramework: AuthenticationFramework,
        userProfileRepository: UserProfileRepository,
    ): SignUpSideEffect = SignUpSideEffect(
        signup = authenticationFramework.signUp,
        updateUserProfile = userProfileRepository.patchUserProfileInfo,
        isUserLoggedIn = authenticationFramework.isUserLoggedIn,
    )

    @Provides
    @Singleton
    fun provideLoginSideEffect(
        authenticationFramework: AuthenticationFramework,
        userProfileRepository: UserProfileRepository,
    ): LoginSideEffect = LoginSideEffect(
        login = authenticationFramework.login,
        getUserProfileInfo = userProfileRepository.getUserProfileInfo,
        isUserLoggedIn = authenticationFramework.isUserLoggedIn,
    )

    @Provides
    @Singleton
    fun provideLogoutSideEffect(authenticationFramework: AuthenticationFramework): LogoutSideEffect = LogoutSideEffect(
        logout = {
            authenticationFramework.logout()
        },
    )

    @Provides
    @Singleton
    fun provideProfileStateMachine(
        @Dispatcher(PHDispatchers.DEFAULT) dispatcher: CoroutineDispatcher,
        checkAuthenticationSideEffect: CheckAuthenticationSideEffect,
        validateSignUpFormSideEffect: ValidateSignUpFormSideEffect,
        validatePasswordSideEffect: ValidatePasswordSideEffect,
        signUpSideEffect: SignUpSideEffect,
        loginSideEffect: LoginSideEffect,
        logoutSideEffect: LogoutSideEffect,
    ): ProfileStateMachine = ProfileStateMachine(
        scope = CoroutineScope(dispatcher),
        checkAuthenticationSideEffect = checkAuthenticationSideEffect,
        validatePasswordSideEffect = validatePasswordSideEffect,
        validateSignUpFormSideEffect = validateSignUpFormSideEffect,
        signUpSideEffect = signUpSideEffect,
        loginSideEffect = loginSideEffect,
        logoutSideEffect = logoutSideEffect,
    )
}
