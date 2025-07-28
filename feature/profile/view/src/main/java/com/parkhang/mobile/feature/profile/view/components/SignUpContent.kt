package com.parkhang.mobile.feature.profile.view.components

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.parkhang.mobile.core.designsystem.components.NAVIGATION_BAR_HEIGHT_DP
import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo

@Composable
fun SignUpContent(
    onSignupClicked: (UserProfileInfo, String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val email = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val surname = remember { mutableStateOf("") }
    val profileName = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val province = remember { mutableStateOf("") }
    val gender = remember { mutableStateOf("") }
    val dateOfBirth = remember { mutableStateOf("") }

    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .padding(bottom = NAVIGATION_BAR_HEIGHT_DP),
        verticalArrangement = spacedBy(Layout.Spacing.Small.L),
    ) {
        Column {
            Text(
                text = "Create Your Account",
                style =
                    CustomTextStyle.Heading2.copy(
                        color = CustomColors.Transparencies.White,
                    ),
            )
            Text(
                text =
                    "Create or login to your account to access your profile " +
                        "and meet new friends in nearby parks.",
                style =
                    CustomTextStyle.Body2
                        .copy(color = CustomColors.Transparencies.White),
            )
        }

        FormField(
            text = email,
            onValueChanged = { email.value = it },
            label = "Email",
        )

        Row(
            horizontalArrangement =
                spacedBy(
                    Layout.Spacing.Small.L,
                ),
        ) {
            FormField(
                modifier = Modifier.weight(1f),
                text = name,
                onValueChanged = { name.value = it },
                label = "First Name",
            )
            FormField(
                modifier = Modifier.weight(1f),
                text = surname,
                onValueChanged = { surname.value = it },
                label = "Last Name",
            )
        }

        Row(
            horizontalArrangement =
                spacedBy(
                    Layout.Spacing.Small.L,
                ),
        ) {
            FormField(
                modifier = Modifier.weight(1f),
                text = profileName,
                onValueChanged = { profileName.value = it },
                label = "Profile Name",
            )
            FormField(
                modifier = Modifier.weight(1f),
                text = gender,
                onValueChanged = { gender.value = it },
                label = "Gender",
            )
        }

        FormField(
            text = city,
            onValueChanged = { city.value = it },
            label = "City",
        )
        FormField(
            text = province,
            onValueChanged = { province.value = it },
            label = "Province",
        )

        FormField(
            text = dateOfBirth,
            onValueChanged = { dateOfBirth.value = it },
            label = "Date of Birth",
            supportingText = "Format: YYYY-MM-DD",
        )

        Row(
            horizontalArrangement =
                spacedBy(
                    Layout.Spacing.Small.L,
                ),
        ) {
            FormField(
                modifier = Modifier.weight(1f),
                text = password,
                onValueChanged = { password.value = it },
                label = "Password",
                shouldShowText = false,
            )
            FormField(
                modifier = Modifier.weight(1f),
                text = confirmPassword,
                onValueChanged = { confirmPassword.value = it },
                label = "Confirm",
                shouldShowText = false,
            )
        }

        Button(
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally),
            onClick = {
                val form =
                    UserProfileInfo(
                        email = email.value,
                        name = name.value,
                        surname = surname.value,
                        profileName = profileName.value,
                        city = city.value,
                        province = province.value,
                        gender = gender.value,
                        dateOfBirth = dateOfBirth.value,
                    )
                onSignupClicked(form, password.value, confirmPassword.value)
            },
            colors = ButtonDefaults.buttonColors(containerColor = CustomColors.Primary.DarkGreen),
        ) {
            Text(
                "Signup",
                style =
                    CustomTextStyle.Heading3.copy(
                        color = CustomColors.Transparencies.White,
                    ),
            )
        }
    }
}

@Preview
@Composable
fun SignUpContentPreview() {
    SignUpContent(
        onSignupClicked = { _, _, _ -> },
    )
}
