package com.parkhang.mobile.feature.profile.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.parkhang.core.designsystem.layout.Layout
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle
import com.parkhang.mobile.core.designsystem.components.FormField
import com.parkhang.mobile.core.designsystem.components.NAVIGATION_BAR_HEIGHT_DP
import com.parkhang.mobile.core.userprofile.entity.UserProfileInfo
import com.parkhang.mobile.feature.profile.di.statemachine.PasswordFormErrorCode
import com.parkhang.mobile.feature.profile.di.statemachine.SignUpFormErrorCode

@Composable
fun SignUpContent(
    onPasswordEdited: (String) -> Unit,
    passwordFormErrorCodeList: List<PasswordFormErrorCode>,
    formErrorCode: SignUpFormErrorCode?,
    onSignupClicked: (UserProfileInfo, String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var dateOfBirthIsSelected by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val email = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val surname = remember { mutableStateOf("") }
    val profileName = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val province = remember { mutableStateOf("") }
    var dateOfBirth = remember { mutableStateOf("") }

    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    val isPasswordFocused = remember { mutableStateOf(false) }

    val imeInsets = WindowInsets.ime
    val imeBottomPaddingDp = imeInsets.asPaddingValues().calculateBottomPadding()
    val isImeVisible = imeBottomPaddingDp > 0.dp

    val emailBringIntoViewRequester = remember { BringIntoViewRequester() }
    val passwordBringIntoViewRequester = remember { BringIntoViewRequester() }

    LaunchedEffect(isImeVisible) {
        if (isPasswordFocused.value && isImeVisible) {
            scrollState.animateScrollTo(
                value = scrollState.maxValue,
            )
        }
    }

    LaunchedEffect(formErrorCode) {
        when (formErrorCode) {
            SignUpFormErrorCode.InvalidEmail -> {
                emailBringIntoViewRequester.bringIntoView()
            }
            SignUpFormErrorCode.PasswordMismatch -> {
                passwordBringIntoViewRequester.bringIntoView()
            }
            else -> {
                // No action needed for other error codes
            }
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .padding(bottom = NAVIGATION_BAR_HEIGHT_DP)
                .imePadding(),
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
                    "Create an account and meet new friends in nearby parks.",
                style =
                    CustomTextStyle.Body2
                        .copy(color = CustomColors.Transparencies.White),
            )
        }

        Column {
            Text(
                text = "* Required fields",
                style =
                    CustomTextStyle.Body5.copy(
                        color = CustomColors.Transparencies.White,
                    ),
            )
            FormField(
                modifier =
                    Modifier
                        .bringIntoViewRequester(emailBringIntoViewRequester),
                text = email,
                onValueChanged = { email.value = it },
                label = "Email*",
                hasError = formErrorCode == SignUpFormErrorCode.InvalidEmail,
                supportingText =
                    if (formErrorCode == SignUpFormErrorCode.InvalidEmail) {
                        "Please enter a valid email address."
                    } else {
                        ""
                    },
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
                modifier =
                    Modifier
                        .weight(1f)
                        .clickable {
                        },
                text = dateOfBirth,
                onValueChanged = { dateOfBirth.value = it },
                label = "Date of Birth",
                supportingText = "Format: YYYY-MM-DD",
                onFocusChanged = { isFocused ->
                    if (isFocused) {
                        dateOfBirthIsSelected = true
                    } else if (dateOfBirthIsSelected) {
                        dateOfBirthIsSelected = false
                    }
                },
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
                text = city,
                onValueChanged = { city.value = it },
                label = "City",
            )
            FormField(
                modifier = Modifier.weight(1f),
                text = province,
                onValueChanged = { province.value = it },
                label = "Province",
            )
        }
        FormField(
            text = password,
            onValueChanged = {
                onPasswordEdited(it)
                password.value = it
            },
            label = "Password*",
            shouldShowText = false,
            onFocusChanged = { isFocused ->
                isPasswordFocused.value = isFocused
            },
        )
        FormField(
            modifier =
                Modifier
                    .bringIntoViewRequester(passwordBringIntoViewRequester),
            text = confirmPassword,
            onValueChanged = { confirmPassword.value = it },
            label = "Confirm password*",
            shouldShowText = false,
            hasError = formErrorCode == SignUpFormErrorCode.PasswordMismatch,
            supportingText =
                if (formErrorCode == SignUpFormErrorCode.PasswordMismatch) {
                    "Passwords do not match."
                } else {
                    ""
                },
        )
        PasswordChecks(
            passwordFormErrorCode = passwordFormErrorCodeList,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Padding.Small.M,
                    ),
        )

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
                        dateOfBirth = dateOfBirth.value,
                    )
                onSignupClicked(form, password.value, confirmPassword.value)
            },
            enabled =
                email.value.isNotBlank() &&
                    password.value.isNotBlank() &&
                    confirmPassword.value.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = CustomColors.Primary.DarkGreen),
        ) {
            Text(
                "Sign Up",
                style =
                    CustomTextStyle.Heading3.copy(
                        color = CustomColors.Transparencies.White,
                    ),
            )
        }
    }
    if (dateOfBirthIsSelected) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            DatePickerDialog(
                onDateSelected = { selectedDate ->
                    dateOfBirth.value = selectedDate
                    dateOfBirthIsSelected = false
                },
                onCancelled = {
                    dateOfBirthIsSelected = false
                },
            )
        }
    }
}

@Preview
@Composable
fun SignUpContentPreview() {
    SignUpContent(
        onPasswordEdited = { },
        passwordFormErrorCodeList =
            listOf(
                PasswordFormErrorCode.TooShort,
                PasswordFormErrorCode.MissingNumber,
            ),
        onSignupClicked = { _, _, _ -> },
        formErrorCode = SignUpFormErrorCode.InvalidEmail,
    )
}
