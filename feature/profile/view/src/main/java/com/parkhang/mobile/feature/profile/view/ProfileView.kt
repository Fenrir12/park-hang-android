package com.parkhang.mobile.feature.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
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
        email = uiState.username ?: "",
        onSignupClicked = viewModel::signUp,
        onLoginClicked = viewModel::login,
        onLogoutClicked = viewModel::logout,
    )
}

@Composable
fun ProfileScreen(
    isUserLoggedIn: Boolean,
    email: String,
    onSignupClicked: (email: String, password: String) -> Unit,
    onLoginClicked: (email: String, password: String) -> Unit,
    onLogoutClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(
                    color = CustomColors.Primary.LightGreen,
                ).padding(
                    horizontal = Padding.Small.L,
                    vertical = Padding.Large.M,
                ),
    ) {
        ProfileContent(
            modifier =
                Modifier
                    .fillMaxSize(),
            isUserLoggedIn = isUserLoggedIn,
            email = email,
            onSignup = onSignupClicked,
            onLogin = onLoginClicked,
            onLogoutClicked = onLogoutClicked,
        )
    }
}

@Composable
fun ProfileContent(
    isUserLoggedIn: Boolean,
    email: String,
    onSignup: (email: String, password: String) -> Unit,
    onLogin: (email: String, password: String) -> Unit,
    onLogoutClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize(),
        verticalArrangement = spacedBy(Padding.Medium.S),
    ) {
        if (!isUserLoggedIn) {
            AnonymousProfileContent(
                onSignup = onSignup,
                onLogin = onLogin,
            )
        } else {
            AuthenticatedProfileContent(
                email = email,
                onLogoutClicked = onLogoutClicked,
            )
        }
    }
}

@Preview
@Composable
fun ProfileViewUnauthenticatedPreview() {
    ProfileScreen(
        onSignupClicked = { _, _ -> },
        onLoginClicked = { _, _ -> },
        onLogoutClicked = { },
        isUserLoggedIn = false,
        email = "",
        modifier =
            Modifier
                .fillMaxSize(),
    )
}

@Preview
@Composable
fun ProfileViewAuthenticatedPreview() {
    ProfileScreen(
        onSignupClicked = { _, _ -> },
        onLoginClicked = { _, _ -> },
        onLogoutClicked = { },
        isUserLoggedIn = true,
        email = "john.doe@gmail.com",
        modifier =
            Modifier
                .fillMaxSize(),
    )
}
