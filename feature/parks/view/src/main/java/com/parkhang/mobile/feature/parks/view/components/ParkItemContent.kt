package com.parkhang.mobile.feature.parks.view.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.parkhang.core.designsystem.layout.CornerRadius
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle
import com.parkhang.mobile.core.designsystem.components.conditional

@SuppressLint("DefaultLocale")
@Composable
fun ParkItemContent(
    parkId: String,
    parkName: String,
    parkDistance: Int,
    isSelected: Boolean,
    onParkCardClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = CustomColors.Transparencies.White,
    subTextColor: Color = CustomColors.Neutrals.White60,
    selectedTextColor: Color = CustomColors.Transparencies.DarkGray,
    selectedSubtextColor: Color = CustomColors.Transparencies.MediumGray,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { onParkCardClicked(parkId) },
                ).background(
                    color = if (isSelected) CustomColors.Primary.VeryLightGreen else CustomColors.Primary.Green,
                    shape = CornerRadius.Medium,
                ).conditional(isSelected) {
                    Modifier
                        .border(
                            width = Padding.Small.Xs,
                            color = CustomColors.Primary.DarkGreen,
                            shape = CornerRadius.Medium,
                        )
                },
        horizontalArrangement = Arrangement.Start,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(vertical = Padding.Small.M, horizontal = Padding.Small.L),
            verticalArrangement = spacedBy(Padding.Small.XXXs),
        ) {
            Text(
                text = parkName,
                style = CustomTextStyle.Heading4
                    .copy(color = if (isSelected) selectedTextColor else textColor),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier,
            )
            val parkDistanceText =
                if (parkDistance < 1000f) {
                    "$parkDistance m"
                } else {
                    val km = parkDistance / 1000f
                    String.format("%.1f km", km)
                }
            Text(
                text = parkDistanceText,
                style = CustomTextStyle.Body2
                    .copy(color = if (isSelected) selectedSubtextColor else subTextColor),
                modifier = Modifier,
            )
        }
    }
}

@Preview
@Composable
fun ParkItemPreviewShort() {
    ParkItemContent(
        parkId = "135135",
        parkName = "Central Park",
        parkDistance = 245,
        onParkCardClicked = {},
        isSelected = false,
    )
}

@Preview
@Composable
fun ParkItemPreviewLong() {
    ParkItemContent(
        parkId = "135135",
        parkName = "Very long park name that exceeds the usual length",
        parkDistance = 1557,
        onParkCardClicked = {},
        isSelected = true,
    )
}
