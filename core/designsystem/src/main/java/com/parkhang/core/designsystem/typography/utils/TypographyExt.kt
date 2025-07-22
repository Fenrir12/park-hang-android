package com.parkhang.core.designsystem.typography.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle

/**
 * Converts a [TextStyle] to a [SpanStyle] while preserving key typographic attributes.
 *
 * This extension function allows you to easily transform a [TextStyle] into a [SpanStyle],
 * maintaining attributes such as `fontSize`, `fontWeight`, `fontStyle`, `letterSpacing`,
 * `background`, and `textDecoration`, while allowing a customizable text `color`.
 *
 * ### Example Usage:
 * ```kotlin
 * val spanStyle = CustomTextStyle.Heading0.toSpanStyle(color =  CustomColors.Transparencies.White)
 * ```
 *
 * @receiver The [TextStyle] to be converted.
 * @param color The color to be applied to the resulting [SpanStyle].
 * @return A new [SpanStyle] instance with the inherited attributes from [TextStyle] and the specified `color`.
 */
@Composable
fun TextStyle.toSpanStyle(color: Color): SpanStyle = this.copy(color = color).toSpanStyle()
