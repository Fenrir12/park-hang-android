package com.parkhang.mobile.feature.profile.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.parkhang.core.designsystem.icons.Icons
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle

@Composable
fun AuthenticatedProfileContent(
    email: String,
    onLogoutClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement =
            spacedBy(
                space = Padding.Medium.S,
            ),
    ) {
        Text(
            text = "User Profile",
            style =
                CustomTextStyle.Heading2.copy(
                    color = CustomColors.Transparencies.White,
                ),
        )
        Text(
            text = email,
            style =
                CustomTextStyle.Body2.copy(
                    color = CustomColors.Transparencies.White,
                ),
        )
        Icon(
            modifier =
                Modifier
                    .clickable(
                        onClick = onLogoutClicked,
                    ),
            painter = painterResource(id = Icons.Navigation.Exit),
            tint = CustomColors.Transparencies.White,
            contentDescription = "Exit Icon",
        )
    }
}

@Preview
@Composable
fun AuthenticatedProfileContentPreview() {
    AuthenticatedProfileContent(
        email = "john.doe@gmail.ca",
        onLogoutClicked = { },
    )
}
