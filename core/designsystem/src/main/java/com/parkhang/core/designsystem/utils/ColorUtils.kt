package com.parkhang.core.designsystem.utils

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.parkhang.core.designsystem.theme.Opacity

/**
 * Determine the color of the item depending on the availability and interaction source.
 * @param available: Availability of the item.
 * @param interactionSource: Interaction source of the item.
 * @param color: Color to use in the default state.
 * @param opacityAvailable: Opacity of the color when the item is not available.
 * @param opacityInteractionSource: Opacity of the color when the item is pressed.
 * @return [Color] of the item.
 */
@Composable
fun getItemColor(
    available: Available = true,
    interactionSource: InteractionSource,
    color: Color = Color.Unspecified,
    opacityAvailable: Float = Opacity.SEVENTY_PERCENT,
    opacityInteractionSource: Float = Opacity.SIXTY_PERCENT,
): Color {
    val interactionColor =
        interactionSource.color(
            color = color,
            opacity = opacityInteractionSource,
        )

    return available.color(
        color = if (available) interactionColor else color,
        opacity = opacityAvailable,
    )
}

/**
 * Get the color of the item depending on the availability.
 * @param color: Color to use in the default state.
 * @param opacity: Opacity of the color when the item is not available.
 * @return [Color] of the item.
 */
@Composable
private fun Available.color(
    color: Color,
    opacity: Float = Opacity.SEVENTY_PERCENT,
): Color = if (this) {
    color
} else {
    color.copy(alpha = opacity)
}

/**
 * Get the color of the item depending on the interaction source.
 * @param color: Color to use in the default state.
 * @param opacity: Opacity of the color when the item is pressed.
 * @return [Color] of the item.
 */
@Composable
private fun InteractionSource.color(
    color: Color,
    opacity: Float,
): Color = if (this.collectIsPressedAsState().value) {
    color.copy(alpha = opacity)
} else {
    color
}

typealias Available = Boolean
