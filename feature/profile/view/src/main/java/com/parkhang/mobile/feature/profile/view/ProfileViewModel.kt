package com.parkhang.mobile.feature.profile.view

import androidx.lifecycle.ViewModel
import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import com.parkhang.mobile.feature.profile.di.statemachine.ProfileStateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val profileStateMachine: ProfileStateMachine,
    ) : ViewModel() {
        val uiStateFlow = profileStateMachine.uiStateFlow

        fun validatePassword(password: String) {
            profileStateMachine.processIntent(
                ProfileStateMachine.UiIntent.ValidatePassword(
                    password = password,
                ),
            )
        }

        fun validateSignUpForm(
            newUserFormInfo: UserProfileInfo,
            password: String,
            confirmPassword: String,
        ) {
            profileStateMachine.processIntent(
                ProfileStateMachine.UiIntent.ValidateSignUpForm(
                    newUserFormInfo = newUserFormInfo,
                    password = password,
                    confirmPassword = confirmPassword,
                ),
            )
        }

        fun login(
            email: String,
            password: String,
        ) {
            profileStateMachine.processIntent(
                ProfileStateMachine.UiIntent.Login(
                    email = email,
                    password = password,
                ),
            )
        }

        fun logout() {
            profileStateMachine.processIntent(
                ProfileStateMachine.UiIntent.Logout,
            )
        }
    }
