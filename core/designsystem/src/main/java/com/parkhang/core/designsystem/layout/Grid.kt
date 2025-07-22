package com.parkhang.core.designsystem.layout

import androidx.compose.ui.unit.Dp
import com.parkhang.core.designsystem.utils.toDp
import com.parkhang.core.designsystem.utils.toPx

/**
 * Defines the [Grid] values.
 *
 * The grid system is made up of 12 columns, and the width of columns is calculated dynamically
 * based on the screen width, gutter, and margin following this formula :
 *
 * column width = device.screen.width - (2 margins * 16px) - (11 gutters * 12 px) / 12 columns
 *
 * The [Grid] values are used to set the width of the composable elements.
 *
 */
sealed class Grid {
    abstract fun getWidth(screenWidth: Dp): Dp

    data object ColumnOne : Grid() {
        override fun getWidth(screenWidth: Dp): Dp = getWidth(screenWidth = screenWidth, columnNumber = 1)
    }

    data object ColumnTwo : Grid() {
        override fun getWidth(screenWidth: Dp): Dp = getWidth(screenWidth = screenWidth, columnNumber = 2)
    }

    data object ColumnThree : Grid() {
        override fun getWidth(screenWidth: Dp): Dp = getWidth(screenWidth = screenWidth, columnNumber = 3)
    }

    data object ColumnFour : Grid() {
        override fun getWidth(screenWidth: Dp): Dp = getWidth(screenWidth = screenWidth, columnNumber = 4)
    }

    data object ColumnFive : Grid() {
        override fun getWidth(screenWidth: Dp): Dp = getWidth(screenWidth = screenWidth, columnNumber = 5)
    }

    data object ColumnSix : Grid() {
        override fun getWidth(screenWidth: Dp): Dp = getWidth(screenWidth = screenWidth, columnNumber = 6)
    }

    data object ColumnSeven : Grid() {
        override fun getWidth(screenWidth: Dp): Dp = getWidth(screenWidth = screenWidth, columnNumber = 7)
    }

    data object ColumnEight : Grid() {
        override fun getWidth(screenWidth: Dp): Dp = getWidth(screenWidth = screenWidth, columnNumber = 8)
    }

    data object ColumnNine : Grid() {
        override fun getWidth(screenWidth: Dp): Dp = getWidth(screenWidth = screenWidth, columnNumber = 9)
    }

    data object ColumnTen : Grid() {
        override fun getWidth(screenWidth: Dp): Dp = getWidth(screenWidth = screenWidth, columnNumber = 10)
    }

    data object ColumnEleven : Grid() {
        override fun getWidth(screenWidth: Dp): Dp = getWidth(screenWidth = screenWidth, columnNumber = 11)
    }

    data object ColumnTwelve : Grid() {
        override fun getWidth(screenWidth: Dp): Dp = getWidth(screenWidth = screenWidth, columnNumber = 12)
    }
}

private fun getWidth(
    screenWidth: Dp,
    columnNumber: Int,
): Dp {
    val columns = 12
    val margins = 2

    val marginWidth = Padding.Small.L.toPx()
    val gutterWidth = Padding.Small.M.toPx()

    val availableWidth = screenWidth.toPx() - ((gutterWidth * (columns - 1)) + (marginWidth * margins))
    val singleColumnWidth = (availableWidth.toFloat()) / columns.toFloat()

    val allColumnsWidth = singleColumnWidth * columnNumber
    val allSpacingsWith = gutterWidth * (columnNumber - 1)

    return (allColumnsWidth + allSpacingsWith).toDp()
}
