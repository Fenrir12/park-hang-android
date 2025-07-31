package com.parkhang.mobile.feature.view.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.parkhang.core.designsystem.icons.Icons
import com.parkhang.core.designsystem.theme.CustomColors.Transparencies.White
import com.parkhang.core.designsystem.typography.CustomTextStyle
import com.parkhang.mobile.core.designsystem.components.TopBar
import com.parkhang.mobile.core.designsystem.components.TopBarButton
import com.parkhang.mobile.core.designsystem.components.TopBarButtonIcon

@Composable
fun HangoutTopBar(
    onChangeHangoutView: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
) {
    TopBar(
        modifier = modifier,
        title = {
            Text(
                text = title ?: "Unknown Park",
                style = CustomTextStyle.Heading2
                    .copy(color = White),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        centeredTitle = true,
        actions = {
            TopBarButton(
                icon = {
                    TopBarButtonIcon(
                        icon = Icons.Navigation.Options.outlined,
                        tintColor = White,
                        contentDescription = "options",
                    )
                },
                onClick = onChangeHangoutView,
                addBorder = false,
            )
        },
    )
}

@Preview
@Composable
private fun ProfileTopBarPreview() {
    HangoutTopBar(
        title = "Unknown Park",
        onChangeHangoutView = { /* No-op */ },
    )
}
