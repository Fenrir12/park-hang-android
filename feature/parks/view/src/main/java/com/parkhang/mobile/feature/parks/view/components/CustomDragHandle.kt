package com.parkhang.mobile.feature.parks.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.parkhang.core.designsystem.theme.CustomColors

@Composable
fun CustomDragHandle(
    modifier: Modifier = Modifier,
    width: Dp = DockedDragHandleWidth,
    height: Dp = DockedDragHandleHeight,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    color: Color = CustomColors.Primary.DarkGreen,
    backgroundColor: Color = CustomColors.Primary.LightGreen,
) {
    Surface(
        modifier =
            modifier
                .background(color = backgroundColor)
                .padding(
                    vertical = DragHandleVerticalPadding,
                ),
        color = color,
        shape = shape,
    ) {
        Box(
            modifier =
                Modifier
                    .size(width = width, height = height),
        )
    }
}

@Preview
@Composable
fun CustomDragHandlePreview() {
    CustomDragHandle(
        width = DockedDragHandleWidth,
        height = DockedDragHandleHeight,
        shape = MaterialTheme.shapes.extraLarge,
        color = CustomColors.Primary.DarkGreen,
        backgroundColor = CustomColors.Primary.LightGreen,
    )
}

private val DockedDragHandleHeight = 4.0.dp
private val DockedDragHandleWidth = 32.0.dp
private val DragHandleVerticalPadding = 22.dp
