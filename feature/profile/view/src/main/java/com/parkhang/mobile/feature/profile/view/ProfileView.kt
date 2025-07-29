package com.parkhang.mobile.feature.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import com.parkhang.mobile.feature.profile.di.statemachine.PasswordFormErrorCode
import com.parkhang.mobile.feature.profile.di.statemachine.SignUpFormErrorCode
import com.parkhang.mobile.feature.profile.view.components.AnonymousProfileContent
import com.parkhang.mobile.feature.profile.view.components.AuthenticatedProfileContent

@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiStateFlow.collectAsState()

    ProfileScreen(
        modifier =
            modifier
                .fillMaxSize(),
        isUserLoggedIn = uiState.isLoggedIn == true,
        email = uiState.userProfileInfo?.email ?: "",
        profileName = uiState.userProfileInfo?.profileName ?: uiState.userProfileInfo?.email ?: "",
        onPasswordEdited = viewModel::validatePassword,
        passwordFormErrorCodeList = uiState.passwordErrorCodeList,
        formErrorCode = uiState.formErrorCode,
        onSignupClicked = viewModel::validateSignUpForm,
        onLoginClicked = viewModel::login,
        onLogoutClicked = viewModel::logout,
    )
}

@Composable
fun ProfileScreen(
    isUserLoggedIn: Boolean,
    email: String,
    profileName: String,
    onPasswordEdited: (String) -> Unit,
    onSignupClicked: (newUserFormInfo: UserProfileInfo, password: String, confirmPassword: String) -> Unit,
    onLoginClicked: (email: String, password: String) -> Unit,
    onLogoutClicked: () -> Unit,
    modifier: Modifier = Modifier,
    passwordFormErrorCodeList: List<PasswordFormErrorCode> = emptyList(),
    formErrorCode: SignUpFormErrorCode? = null,
) {
    Box(
        modifier =
            modifier
                .background(
                    color = CustomColors.Primary.LightGreen,
                ).padding(
                    horizontal = Padding.Small.L,
                    vertical = Padding.Small.S,
                ),
    ) {
        ProfileContent(
            modifier =
                Modifier
                    .fillMaxSize(),
            isUserLoggedIn = isUserLoggedIn,
            email = email,
            profileName = profileName,
            onPasswordEdited = onPasswordEdited,
            passwordFormErrorCodeList = passwordFormErrorCodeList,
            formErrorCode = formErrorCode,
            onSignupClicked = onSignupClicked,
            onLoginClicked = onLoginClicked,
            onLogoutClicked = onLogoutClicked,
        )
    }
}

@Composable
fun ProfileContent(
    isUserLoggedIn: Boolean,
    email: String,
    profileName: String,
    onPasswordEdited: (String) -> Unit,
    passwordFormErrorCodeList: List<PasswordFormErrorCode>,
    formErrorCode: SignUpFormErrorCode?,
    onSignupClicked: (newUserFormInfo: UserProfileInfo, password: String, confirmPassword: String) -> Unit,
    onLoginClicked: (email: String, password: String) -> Unit,
    onLogoutClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
        verticalArrangement = spacedBy(Padding.Medium.S),
    ) {
        if (!isUserLoggedIn) {
            AnonymousProfileContent(
                onPasswordEdited = onPasswordEdited,
                passwordFormErrorCodeList = passwordFormErrorCodeList,
                formErrorCode = formErrorCode,
                onSignupClicked = onSignupClicked,
                onLoginClicked = onLoginClicked,
            )
        } else {
            AuthenticatedProfileContent(
                email = email,
                profileName = profileName,
                onLogoutClicked = onLogoutClicked,
            )
        }
    }
}

@Preview
@Composable
fun ProfileViewUnauthenticatedPreview() {
    ProfileScreen(
        onPasswordEdited = { },
        onSignupClicked = { _, _, _ -> },
        onLoginClicked = { _, _ -> },
        onLogoutClicked = { },
        isUserLoggedIn = false,
        email = "",
        profileName = "",
        modifier =
            Modifier
                .fillMaxSize(),
    )
}

@Preview
@Composable
fun ProfileViewAuthenticatedPreview() {
    ProfileScreen(
        onPasswordEdited = { },
        onSignupClicked = { _, _, _ -> },
        onLoginClicked = { _, _ -> },
        onLogoutClicked = { },
        isUserLoggedIn = true,
        email = "john.doe@gmail.com",
        profileName = "John Doe",
        modifier =
            Modifier
                .fillMaxSize(),
    )
}
