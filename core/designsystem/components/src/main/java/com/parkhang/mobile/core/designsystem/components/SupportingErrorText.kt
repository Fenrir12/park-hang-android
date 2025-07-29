package com.parkhang.mobile.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.parkhang.core.designsystem.icons.Icons
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle

@Composable
fun SupportingTextWithIcon(
    text: String,
    @DrawableRes icon: Int? = Icons.Functional.Warning,
    modifier: Modifier = Modifier,
    color: Color = CustomColors.Status.Error,
) {
    Row(
        modifier =
            modifier
                .padding(horizontal = Padding.Small.Xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.let {
            Icon(
                modifier =
                    Modifier
                        .padding(end = Padding.Small.XXs),
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = color,
            )
        }
        Text(
            text = text,
            style =
                CustomTextStyle.Body3.copy(
                    color = color,
                ),
        )
    }
}

@Preview
@Composable
fun SupportingTextWithIconPreview() {
    SupportingTextWithIcon(
        text = "This is an error message",
        icon = Icons.Functional.Warning,
        modifier = Modifier,
        color = CustomColors.Status.Error,
    )
}
