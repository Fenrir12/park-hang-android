package com.parkhang.mobile.feature.profile.view.components

import androidx.compose.foundation.layout.Column
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
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle
import com.parkhang.mobile.core.designsystem.components.FormField

@Composable
fun LoginContent(
    onLoginClicked: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var email = ""
    var password = ""
    Column(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        Column {
            Text(
                text = "Log in to your Account",
                style =
                    CustomTextStyle.Heading2.copy(
                        color = CustomColors.Transparencies.White,
                    ),
            )
            Text(
                text =
                    "Login to your account to access your profile " +
                        "and meet new friends in nearby parks.",
                style =
                    CustomTextStyle.Body2
                        .copy(color = CustomColors.Transparencies.White),
            )
        }
        Spacer(
            modifier =
                Modifier
                    .size(Layout.Spacing.Medium.M),
        )
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
        Button(
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally),
            onClick = { onLoginClicked(email, password) },
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = CustomColors.Primary.Green,
                ),
        ) {
            Text(
                text = "Login",
                style =
                    CustomTextStyle.Heading2
                        .copy(color = CustomColors.Transparencies.White),
            )
        }
    }
}

@Preview
@Composable
fun LoginContentPreview() {
    LoginContent(
        onLoginClicked = { _, _ -> },
        modifier = Modifier,
    )
}
