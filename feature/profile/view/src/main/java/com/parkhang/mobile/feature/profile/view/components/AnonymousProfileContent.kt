package com.parkhang.mobile.feature.profile.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import kotlinx.coroutines.launch

@Composable
fun AnonymousProfileContent(
    onSignupClicked: (newUserFormInfo: UserProfileInfo, password: String, confirmPassword: String) -> Unit,
    onLoginClicked: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState =
        rememberPagerState(
            initialPage = AuthenticationPage.LOGIN.value,
            pageCount = { AuthenticationPage.entries.size },
        )
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AuthenticationPagerSelector(
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = Padding.Medium.S),
            isEnabled = true,
            onAuthenticationChange = { authenticationPage ->
                scope.launch {
                    pagerState.animateScrollToPage(authenticationPage.value)
                }
            },
            currentPage = AuthenticationPage.fromValue(pagerState.currentPage),
        )

        AuthenticationHorizontalPager(
            modifier = modifier,
            pagerState = pagerState,
            onSignupClicked = onSignupClicked,
            onLoginClicked = onLoginClicked,
        )
    }
}

@Preview
@Composable
fun AnonymousProfileContentPreview() {
    AnonymousProfileContent(
        modifier =
            Modifier
                .background(
                    color = CustomColors.Primary.LightGreen,
                ).fillMaxSize(),
        onSignupClicked = { _, _, _ -> },
        onLoginClicked = { _, _ -> },
    )
}
