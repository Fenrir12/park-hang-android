package com.parkhang.mobile.feature.parks.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

fun NavController.navigateToParks(navOptions: NavOptions? = null) {
    navigate(route = ROUTE_PARKS, navOptions = navOptions)
}

fun NavGraphBuilder.parkScreen() {
    composable(
        route = ROUTE_PARKS,
    ) {
        ParkView(
            modifier =
                Modifier
                    .fillMaxSize(),
        )
    }
}

const val ROUTE_PARKS = "parks"
