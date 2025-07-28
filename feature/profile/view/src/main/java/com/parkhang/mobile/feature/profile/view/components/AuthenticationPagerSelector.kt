package com.parkhang.mobile.feature.profile.view.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.mobile.core.designsystem.components.ButtonPill

@Composable
fun AuthenticationPagerSelector(
    currentPage: AuthenticationPage,
    isEnabled: Boolean,
    onAuthenticationChange: (AuthenticationPage) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .background(
                    color = CustomColors.Primary.DarkGreen,
                    shape = ButtonDefaults.textShape,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedButtonPill(
            isSelected = currentPage == AuthenticationPage.LOGIN && isEnabled,
            text = "Login",
            onClicked = {
                if (isEnabled) onAuthenticationChange(AuthenticationPage.LOGIN)
            },
        )
        AnimatedButtonPill(
            isSelected = currentPage == AuthenticationPage.SIGNUP || !isEnabled,
            text = "Sign Up",
            onClicked = {
                if (isEnabled) onAuthenticationChange(AuthenticationPage.SIGNUP)
            },
        )
    }
}

@Composable
internal fun AnimatedButtonPill(
    isSelected: Boolean,
    text: String,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = isSelected,
        transitionSpec = {
            fadeIn(animationSpec = tween(durationMillis = TRANSITION_ANIMATION_DURATION_MILLIS)) togetherWith
                fadeOut(animationSpec = tween(durationMillis = TRANSITION_ANIMATION_DURATION_MILLIS)) using
                SizeTransform(
                    clip = false,
                    sizeAnimationSpec = { _, _ ->
                        spring(
                            dampingRatio = DampingRatioMediumBouncy,
                            stiffness = StiffnessMediumLow,
                            visibilityThreshold = IntSize.VisibilityThreshold,
                        )
                    },
                )
        },
        modifier = modifier,
    ) { selected ->
        ButtonPill(
            text = text,
            onClicked = onClicked,
            containerColor = if (selected) CustomColors.Primary.Green else CustomColors.Primary.DarkGreen,
            addBorder = selected,
            cooldownOnClicked = false,
        )
    }
}

@Preview
@Composable
private fun AuthenticationPagerSelectorPreview() {
    AuthenticationPagerSelector(
        currentPage = AuthenticationPage.LOGIN,
        isEnabled = true,
        onAuthenticationChange = {},
        modifier = Modifier,
    )
}

private const val TRANSITION_ANIMATION_DURATION_MILLIS = 300
