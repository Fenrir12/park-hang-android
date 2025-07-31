package com.parkhang.mobile.feature.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.parkhang.core.designsystem.layout.CornerRadius
import com.parkhang.core.designsystem.layout.Layout
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle
import com.parkhang.mobile.feature.hangout.entity.Hangout

@Composable
fun HangoutItem(
    hangout: Hangout,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = CustomColors.Neutrals.White85,
                shape = CornerRadius.Small,
            ).border(
                width = Layout.Spacing.Small.XXs,
                color = CustomColors.Primary.LightGreen,
                shape = CornerRadius.Small,
            ),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Padding.Small.S,
                    vertical = Padding.Medium.S,
                ),
            verticalArrangement = spacedBy(Padding.Small.Xs),
        ) {
            Text(
                text = hangout.title,
                style = CustomTextStyle.Heading3.copy(
                    color = CustomColors.Transparencies.DarkGray,
                ),
            )
            Text(
                text = hangout.description,
                style = CustomTextStyle.Body1.copy(
                    color = CustomColors.Transparencies.DarkGray,
                ),
            )
            Spacer(
                modifier = Modifier
                    .height(Layout.Spacing.Small.L),
            )
            Text(
                text = hangout.ownerName,
                style = CustomTextStyle.Body1.copy(
                    color = CustomColors.Transparencies.DarkGray,
                ),
            )
        }
    }
}

@Preview
@Composable
fun HangoutItemPreview() {
    HangoutItem(
        hangout = Hangout(
            ownerName = "John Doe",
            title = "Park Picnic",
            description = "Join us for a fun picnic at the park!",
            createdAt = "2023-10-01T12:00:00Z",
        ),
    )
}
