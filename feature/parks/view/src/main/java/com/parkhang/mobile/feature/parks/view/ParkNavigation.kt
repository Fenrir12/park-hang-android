package com.parkhang.mobile.feature.parks.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.parksScreen(onCheckIn: (String, String) -> Unit) {
    composable(
        route = ROUTE_PARKS,
    ) {
        ParksView(
            onCheckIn = onCheckIn,
            modifier =
                Modifier
                    .fillMaxSize(),
        )
    }
}

const val ROUTE_PARKS = "parks"
