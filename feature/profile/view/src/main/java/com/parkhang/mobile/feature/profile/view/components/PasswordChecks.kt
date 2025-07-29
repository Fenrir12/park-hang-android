package com.parkhang.mobile.feature.profile.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.parkhang.core.designsystem.icons.IconSize
import com.parkhang.core.designsystem.icons.Icons
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle
import com.parkhang.mobile.feature.profile.di.statemachine.PasswordFormErrorCode

@Composable
fun PasswordChecks(
    passwordFormErrorCode: List<PasswordFormErrorCode>,
    modifier: Modifier = Modifier,
) {
    val allChecks =
        listOf(
            PasswordFormErrorCode.TooShort,
            PasswordFormErrorCode.MissingCapitalCharacter,
            PasswordFormErrorCode.MissingNumber,
            PasswordFormErrorCode.MissingSymbol,
        )

    Column(modifier = modifier) {
        Text(
            text = "Password Requirements - at least 3 of the following:",
            style =
                CustomTextStyle.Body3.copy(
                    color = CustomColors.Status.Error,
                ),
        )
        allChecks.forEach { errorCode ->
            PasswordCheckItem(
                passwordFormErrorCode = errorCode,
                isValid = !passwordFormErrorCode.contains(errorCode),
            )
        }
    }
}

@Composable
private fun PasswordCheckItem(
    passwordFormErrorCode: PasswordFormErrorCode,
    isValid: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(IconSize.Small.M),
            painter = painterResource(id = Icons.Navigation.Dot.Black),
            tint = if (isValid) CustomColors.Primary.Green else CustomColors.Status.ErrorBG,
            contentDescription = null,
        )
        Text(
            text =
                passwordFormErrorCode.errorMessageFromErrorCode(
                    isValid = isValid,
                ),
            style =
                CustomTextStyle.Body4.copy(
                    color = if (isValid) CustomColors.Primary.Green else CustomColors.Status.ErrorBG,
                    textDecoration = if (isValid) TextDecoration.LineThrough else TextDecoration.None,
                ),
        )
    }
}

private fun PasswordFormErrorCode.errorMessageFromErrorCode(isValid: Boolean): String =
    when (this) {
        PasswordFormErrorCode.TooShort -> "Password is too short"
        PasswordFormErrorCode.MissingSymbol -> "Password must contain a special character"
        PasswordFormErrorCode.MissingNumber -> "Password must contain a number"
        PasswordFormErrorCode.MissingCapitalCharacter -> "Password must contain an uppercase letter"
    }

@Preview
@Composable
fun PasswordChecksPreview() {
    PasswordChecks(
        passwordFormErrorCode =
            listOf(
                PasswordFormErrorCode.MissingSymbol,
                PasswordFormErrorCode.MissingNumber,
                PasswordFormErrorCode.MissingCapitalCharacter,
            ),
    )
}
