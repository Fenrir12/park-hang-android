package com.parkhang.core.designsystem.utils

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Converts dp to px.
 *
 * @return The converted value in px.
 */
fun Dp.toPx(): Int = (this.value * Resources.getSystem().displayMetrics.density).toInt()

/**
 * Converts px to dp.
 *
 * @return The converted value in dp.
 */
fun Float.toDp(): Dp = (this / Resources.getSystem().displayMetrics.density).dp
