package com.parkhang.mobile.feature.parks.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.parkScreen() {
    composable(
        route = ROUTE_PLACES,
    ) {
        ParkView(
            modifier =
                Modifier
                    .fillMaxSize(),
        )
    }
}

const val ROUTE_PLACES = "parks"
