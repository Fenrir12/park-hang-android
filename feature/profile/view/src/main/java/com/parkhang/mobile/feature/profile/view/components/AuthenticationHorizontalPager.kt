package com.parkhang.mobile.feature.profile.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import com.parkhang.mobile.feature.profile.di.statemachine.PasswordFormErrorCode
import com.parkhang.mobile.feature.profile.di.statemachine.SignUpFormErrorCode

@Composable
fun AuthenticationHorizontalPager(
    pagerState: PagerState,
    onPasswordEdited: (String) -> Unit,
    passwordFormErrorCodeList: List<PasswordFormErrorCode>,
    formErrorCode: SignUpFormErrorCode?,
    onSignupClicked: (newUserFormInfo: UserProfileInfo, password: String, confirmPassword: String) -> Unit,
    onLoginClicked: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        modifier =
            modifier
                .fillMaxWidth(),
        state = pagerState,
    ) { page ->
        when (page) {
            AuthenticationPage.LOGIN.value -> {
                LoginContent(
                    onLoginClicked = onLoginClicked,
                )
            }
            AuthenticationPage.SIGNUP.value -> {
                SignUpContent(
                    onPasswordEdited = onPasswordEdited,
                    passwordFormErrorCodeList = passwordFormErrorCodeList,
                    onSignupClicked = onSignupClicked,
                    formErrorCode = formErrorCode,
                )
            }
        }
    }
}

enum class AuthenticationPage(
    val value: Int,
) {
    LOGIN(0),
    SIGNUP(1),
    ;

    companion object {
        val entries = AuthenticationPage.entries.toTypedArray()

        fun fromValue(value: Int): AuthenticationPage = entries.firstOrNull { it.value == value } ?: LOGIN
    }
}
