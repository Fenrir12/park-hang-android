package com.parkhang.mobile.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.theme.CustomColors.Transparencies
import com.parkhang.core.designsystem.typography.CustomTextStyle
import com.parkhang.mobile.core.common.BottomNavItem

@Composable
fun RowScope.BottomNavigationItem(
    item: BottomNavItem,
    onNavigationItemSelected: (BottomNavItem) -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .defaultMinSize(minHeight = NAVIGATION_BAR_HEIGHT_DP)
                .weight(weight = NAVIGATION_ITEM_BADGE_WIGHT)
                .wrapContentSize(),
    ) {
        Row(
            modifier =
                Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onNavigationItemSelected(item)
                },
                icon = {
                    ItemIcon(
                        icon = item.icon,
                        contentDescription = item.title,
                    )
                },
                label = {
                    ItemLabel(
                        title = item.title,
                        isSelected = isSelected,
                    )
                },
                alwaysShowLabel = true,
                colors =
                    NavigationBarItemDefaults
                        .colors(
                            selectedIconColor = Transparencies.White,
                            indicatorColor = CustomColors.Primary.Green,
                        ),
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun ItemIcon(
    @DrawableRes icon: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
        )
    }
}

@Composable
private fun ItemLabel(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = title,
        style = CustomTextStyle.Body5,
        color = if (isSelected) Transparencies.White else Transparencies.Gray,
    )
}

@Preview
@Composable
private fun PreviewBottomNavigationItem() {
    Row(
        modifier =
            Modifier
                .wrapContentSize(),
    ) {
        BottomNavigationItem(
            item = BottomNavItem.Hangout,
            onNavigationItemSelected = {},
            isSelected = true,
        )
    }
}

val NAVIGATION_BAR_HEIGHT_DP = 64.dp
private const val NAVIGATION_ITEM_BADGE_WIGHT = 1f
