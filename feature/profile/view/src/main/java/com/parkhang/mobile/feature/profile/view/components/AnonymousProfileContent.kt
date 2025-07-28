package com.parkhang.mobile.feature.profile.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.parkhang.core.designsystem.layout.Layout
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle
import com.parkhang.mobile.core.designsystem.components.FormField

@Composable
fun AnonymousProfileContent(
    onSignup: (email: String, password: String) -> Unit,
    onLogin: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize(),
        verticalArrangement = spacedBy(Padding.Small.S),
    ) {
        Text(
            text = "Login or Sign Up",
            style =
                CustomTextStyle.Heading1
                    .copy(color = CustomColors.Transparencies.White),
        )
        Text(
            text =
                "Create or login to your account to access your profile " +
                    "and meet new friends in nearby parks.",
            style =
                CustomTextStyle.Body2
                    .copy(color = CustomColors.Transparencies.White),
        )
        Spacer(
            modifier =
                Modifier
                    .size(Layout.Spacing.Medium.L),
        )
        SignupOrLogin(
            onSignup = onSignup,
            onLogin = onLogin,
        )
    }
}

@Composable
private fun SignupOrLogin(
    onSignup: (email: String, password: String) -> Unit,
    onLogin: (email: String, password: String) -> Unit,
) {
    Column {
        var email = ""
        var password = ""
        FormField(
            text = remember { mutableStateOf("") },
            onValueChanged = { newText -> email = newText },
            label = "Email",
        )
        Spacer(
            modifier =
                Modifier
                    .size(Layout.Spacing.Medium.M),
        )
        FormField(
            text = remember { mutableStateOf(password) },
            onValueChanged = { newText -> password = newText },
            shouldShowText = false,
            label = "Password",
            supportingText = "Please enter your email address in format: yourname@example.com",
            onFocusChanged = { isFocused -> },
        )
        Spacer(
            modifier =
                Modifier
                    .size(Layout.Spacing.Medium.M),
        )
        Row(
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally),
            horizontalArrangement = spacedBy(Layout.Spacing.Medium.L),
        ) {
            Button(
                onClick = { onLogin(email, password) },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = CustomColors.Primary.Green,
                    ),
            ) {
                Text(
                    text = "Login",
                    style =
                        CustomTextStyle.Body1
                            .copy(color = CustomColors.Transparencies.White),
                )
            }
            Button(
                onClick = { onSignup(email, password) },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = CustomColors.Primary.DarkGreen,
                    ),
            ) {
                Text(
                    text = "Signup",
                    style =
                        CustomTextStyle.Body1
                            .copy(color = CustomColors.Transparencies.White),
                )
            }
        }
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
        onSignup = { _, _ -> },
        onLogin = { _, _ -> },
    )
}
