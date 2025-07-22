package com.parkhang.core.designsystem.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Creates [CornerRadius] with the same size applied for all four corners.
 */
object CornerRadius {
    val None: RoundedCornerShape = RoundedCornerShape(0.dp)
    val ExtraSmall: RoundedCornerShape = RoundedCornerShape(4.dp)
    val Small: RoundedCornerShape = RoundedCornerShape(8.dp)
    val Medium: RoundedCornerShape = RoundedCornerShape(12.dp)
    val Large: RoundedCornerShape = RoundedCornerShape(16.dp)
    val ExtraLarge: RoundedCornerShape = RoundedCornerShape(24.dp)
    val Rounded: RoundedCornerShape = RoundedCornerShape(48.dp)

    val values =
        setOf(
            ExtraSmall,
            Small,
            Medium,
            Large,
            ExtraLarge,
            Rounded,
        )
}

/**
 * [Shapes] overrides Jetpack compose default Shapes value.
 */
internal val Shapes =
    Shapes(
        extraSmall = CornerRadius.ExtraSmall,
        small = CornerRadius.Small,
        medium = CornerRadius.Large,
        large = CornerRadius.ExtraLarge,
        extraLarge = CornerRadius.Rounded,
    )

@Preview
@Composable
private fun ShapesPreview() {
    Column {
        CornerRadius.values.forEach {
            CustomCard(
                shape = it,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
            )
        }
    }
}

@Composable
private fun CustomCard(
    shape: Shape,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = shape,
    ) {
        Text(
            text = shape.toString(),
            textAlign = TextAlign.Center,
        )
    }
}
