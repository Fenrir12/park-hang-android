package com.parkhang.mobile.feature.profile.view

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(ROUTE_PROFILE, navOptions)
}

fun NavGraphBuilder.profileScreen() {
    composable(
        route = ROUTE_PROFILE,
    ) {
        ProfileView()
    }
}

const val ROUTE_PROFILE = "profile"
