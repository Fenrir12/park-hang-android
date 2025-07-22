package com.parkhang.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val classicColorScheme =
    darkColorScheme(
        primary = CustomColors.Primary.DarkGreen,
        secondary = CustomColors.Primary.Green,
        tertiary = CustomColors.Primary.LightGreen,
        error = CustomColors.Status.Error,
        onError = CustomColors.Status.ErrorBG,
    )

@Composable
fun ParkHangTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = classicColorScheme,
        content = content,
    )
}
