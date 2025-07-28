package com.parkhang.mobile.feature.profile.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.parkhang.core.designsystem.icons.Icons
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle

@Composable
fun AuthenticatedProfileContent(
    email: String,
    profileName: String,
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
            text = "Profile",
            style =
                CustomTextStyle.Heading2.copy(
                    color = CustomColors.Transparencies.White,
                ),
        )
        ProfileCard(
            icon = Icons.Navigation.Email.White,
            text = email,
        )
        if (profileName.isNotEmpty()) {
            ProfileCard(
                icon = Icons.Navigation.Profile.White,
                text = profileName,
            )
        }
        ProfileCard(
            modifier =
                Modifier
                    .clickable(
                        onClick = onLogoutClicked,
                    ),
            icon = Icons.Navigation.Exit,
            borderColor = CustomColors.Accent.Red,
            containerColor = CustomColors.Accent.RedLight,
        )
    }
}

@Preview
@Composable
fun AuthenticatedProfileContentPreview() {
    AuthenticatedProfileContent(
        email = "john.doe@gmail.ca",
        profileName = "John Doe",
        onLogoutClicked = { },
    )
}
