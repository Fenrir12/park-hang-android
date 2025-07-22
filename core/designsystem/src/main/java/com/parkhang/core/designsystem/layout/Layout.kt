package com.parkhang.core.designsystem.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * [Layout] primitives object.
 */
object Layout {
    /**
     * Defines the [Spacing] values.
     */
    object Spacing {
        object Small {
            val XXXs = 1.dp
            val XXs = 2.dp
            val Xs = 4.dp
            val S = 8.dp
            val M = 12.dp
            val L = 16.dp
        }

        object Medium {
            val S = 24.dp
            val M = 32.dp
            val L = 48.dp
        }

        object Large {
            val S = 64.dp
            val M = 88.dp
            val L = 120.dp
        }

        object Grid {
            val Margin = 24.dp
            val Gutter = 12.dp
        }
    }
}

@Preview
@Composable
private fun LayoutSpacingSmallPreview() {
    Column {
        CustomSpacer(size = Layout.Spacing.Small.Xs)
        Spacer(modifier = Modifier.height(5.dp))
        CustomSpacer(size = Layout.Spacing.Small.S)
        Spacer(modifier = Modifier.height(5.dp))
        CustomSpacer(size = Layout.Spacing.Small.M)
        Spacer(modifier = Modifier.height(5.dp))
        CustomSpacer(size = Layout.Spacing.Small.L)
    }
}

@Preview
@Composable
private fun LayoutSpacingMediumPreview() {
    Column {
        CustomSpacer(size = Layout.Spacing.Medium.S)
        Spacer(modifier = Modifier.height(5.dp))
        CustomSpacer(size = Layout.Spacing.Medium.M)
        Spacer(modifier = Modifier.height(5.dp))
        CustomSpacer(size = Layout.Spacing.Medium.L)
    }
}

@Preview
@Composable
private fun LayoutSpacingLargePreview() {
    Column {
        CustomSpacer(size = Layout.Spacing.Large.S)
        Spacer(modifier = Modifier.height(5.dp))
        CustomSpacer(size = Layout.Spacing.Large.M)
        Spacer(modifier = Modifier.height(5.dp))
        CustomSpacer(size = Layout.Spacing.Large.L)
    }
}

@Preview
@Composable
private fun LayoutSpacingGridPreview() {
    Column {
        repeat(5) {
            CustomSpacer(size = Layout.Spacing.Grid.Margin)
            Spacer(modifier = Modifier.height(Layout.Spacing.Grid.Gutter))
        }
    }
}

@Composable
private fun CustomSpacer(
    size: Dp,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier =
            modifier
                .height(size)
                .fillMaxWidth()
                .background(Color.Red),
    )
}
