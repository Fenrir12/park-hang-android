package com.parkhang.mobile.feature.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.mobile.feature.profile.view.components.AnonymousProfileContent

@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    ProfileScreen(
        modifier =
            modifier
                .fillMaxSize(),
        onSignup = viewModel::signUp,
        onLogin = viewModel::login,
    )
}

@Composable
fun ProfileScreen(
    onSignup: (email: String, password: String) -> Unit,
    onLogin: (email: String, password: String) -> Unit,
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
            onSignup = onSignup,
            onLogin = onLogin,
        )
    }
}

@Composable
fun ProfileContent(
    onSignup: (email: String, password: String) -> Unit,
    onLogin: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize(),
        verticalArrangement = spacedBy(Padding.Medium.S),
    ) {
        AnonymousProfileContent(
            onSignup = onSignup,
            onLogin = onLogin,
        )
    }
}

@Preview
@Composable
fun ProfileViewPreview() {
    ProfileScreen(
        onSignup = { _, _ -> },
        onLogin = { _, _ -> },
        modifier =
            Modifier
                .fillMaxSize(),
    )
}
