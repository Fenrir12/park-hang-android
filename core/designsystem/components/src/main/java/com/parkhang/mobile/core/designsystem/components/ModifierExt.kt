package com.parkhang.mobile.core.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.conditional(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier,
): Modifier = if (condition) {
    then(modifier(Modifier))
} else {
    this
}
