package com.parkhang.mobile.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.parkhang.core.designsystem.layout.Layout
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle
import kotlinx.coroutines.delay

@Composable
fun ButtonPill(
    text: String,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = CustomColors.Primary.Green,
    addBorder: Boolean = true,
    borderColor: Color = CustomColors.Border.StrokeTint,
    textColor: Color = CustomColors.Transparencies.White,
    cooldownDurationMillis: Long = DISABLE_BUTTON_DURATION_MILLIS,
    cooldownOnClicked: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    var isButtonEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(isButtonEnabled) {
        if (isButtonEnabled.not()) {
            delay(cooldownDurationMillis)
            isButtonEnabled = true
        }
    }

    TextButton(
        interactionSource = interactionSource,
        border =
            if (addBorder) {
                BorderStroke(
                    width = Layout.Spacing.Small.XXXs,
                    color = borderColor,
                )
            } else {
                null
            },
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        modifier = modifier,
        enabled = isButtonEnabled,
        onClick = {
            onClicked()
            if (cooldownOnClicked) {
                isButtonEnabled = false
            }
        },
        contentPadding = contentPadding,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier,
                text = text,
                color = textColor,
                style = CustomTextStyle.Heading2,
            )
        }
    }
}

@Preview
@Composable
fun ButtonPillPreview() {
    ButtonPill(
        text = "Click Me",
        onClicked = {},
        modifier = Modifier.padding(Layout.Spacing.Medium.S),
    )
}

private const val DISABLE_BUTTON_DURATION_MILLIS = 1000L
