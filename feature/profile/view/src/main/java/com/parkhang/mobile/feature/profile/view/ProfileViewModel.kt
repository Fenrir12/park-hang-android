package com.parkhang.mobile.feature.profile.view

import androidx.lifecycle.ViewModel
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

        fun signUp(
            email: String,
            password: String,
        ) {
            profileStateMachine.processIntent(
                ProfileStateMachine.UiIntent.SignUp(
                    email = email,
                    password = password,
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
