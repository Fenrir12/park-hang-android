package com.parkhang.mobile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.parkhang.core.designsystem.layout.Layout
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.theme.CustomColors.Neutrals
import com.parkhang.mobile.core.common.BottomNavItem
import com.parkhang.mobile.core.designsystem.components.BottomNavigationItem

@Composable
fun MainNavigationBar(
    bottomNavItemList: List<BottomNavItem>,
    currentDestination: BottomNavItem?,
    onNavigationSelected: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        NavigationBar(
            containerColor = CustomColors.Primary.DarkGreen,
        ) {
            bottomNavItemList.forEach { item ->
                val isSelected = item.route == currentDestination?.route

                BottomNavigationItem(
                    item = item,
                    onNavigationItemSelected = onNavigationSelected,
                    isSelected = isSelected,
                )
            }
        }

        HorizontalDivider(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(Layout.Spacing.Small.XXXs),
            color = Neutrals.White10,
        )
    }
}

@Preview
@Composable
fun MainNavigationBarPreview() {
    MainNavigationBar(
        bottomNavItemList =
            listOf(
                BottomNavItem.Parks,
                BottomNavItem.Profile,
            ),
        currentDestination = BottomNavItem.Parks,
        onNavigationSelected = {},
    )
}
