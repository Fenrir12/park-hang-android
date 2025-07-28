package com.parkhang.mobile.feature.profile.view.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.parkhang.core.designsystem.icons.Icons
import com.parkhang.core.designsystem.layout.CornerRadius
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle
import com.parkhang.mobile.core.designsystem.components.conditional

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    text: String? = null,
    @DrawableRes icon: Int? = null,
    borderColor: Color = CustomColors.Primary.DarkGreen,
    containerColor: Color = CustomColors.Primary.Green,
) {
    Box(
        modifier =
            modifier
                .background(
                    color = containerColor,
                    shape = CornerRadius.Small,
                ).border(
                    width = Padding.Small.XXs,
                    color = borderColor,
                    shape = CornerRadius.Small,
                ),
    ) {
        Row(
            modifier =
                Modifier
                    .padding(
                        horizontal = Padding.Small.S,
                        vertical = Padding.Small.XXs,
                    ),
        ) {
            icon?.let {
                Icon(
                    modifier =
                        Modifier
                            .conditional(text != null) {
                                padding(end = Padding.Small.Xs)
                            },
                    painter = painterResource(id = icon),
                    tint = CustomColors.Transparencies.White,
                    contentDescription = null,
                )
            }
            text?.let {
                Text(
                    text = text,
                    style =
                        CustomTextStyle.Body2.copy(
                            color = CustomColors.Transparencies.White,
                        ),
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileInfoCardPreview() {
    ProfileCard(
        text = "john@doe.ca",
        icon = Icons.Navigation.Email.White,
    )
}
