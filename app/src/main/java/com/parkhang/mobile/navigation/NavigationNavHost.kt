package com.parkhang.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.parkhang.mobile.feature.parks.view.parkScreen
import com.parkhang.mobile.feature.profile.view.profileScreen

@Composable
fun NavigationNavHost(
    startDestination: String,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        startDestination = startDestination,
        navController = navHostController,
        modifier = modifier,
    ) {
        parkScreen()
        profileScreen()
    }
}
