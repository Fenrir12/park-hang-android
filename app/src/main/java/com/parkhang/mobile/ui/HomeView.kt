package com.parkhang.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.parkhang.core.designsystem.theme.CustomColors.Transparencies
import com.parkhang.mobile.core.common.BottomNavItem
import com.parkhang.mobile.core.designsystem.components.NAVIGATION_BAR_HEIGHT_DP

@Composable
fun HomeView(
    navHost: @Composable (modifier: Modifier) -> Unit,
    currentDestination: BottomNavItem?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(color = Transparencies.Black)
                .fillMaxSize(),
    ) {
        navHost(
            Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = NAVIGATION_BAR_HEIGHT_DP)
                .fillMaxSize(),
        )
        MainNavigationBar(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter),
            bottomNavItemList = BottomNavItem.values().toList(),
            currentDestination = currentDestination,
            onNavigationSelected = {},
        )
    }
}
