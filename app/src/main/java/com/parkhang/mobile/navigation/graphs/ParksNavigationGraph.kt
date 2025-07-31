package com.parkhang.mobile.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.parkhang.mobile.core.common.BASE_ROUTE
import com.parkhang.mobile.core.common.PARKS_GRAPH_ROUTE
import com.parkhang.mobile.feature.parks.view.ROUTE_PARKS

fun NavController.navigateToParksGraph(navOptions: NavOptions? = null) {
    this.navigate(PARKS_GRAPH_ROUTE, navOptions)
}

fun NavGraphBuilder.parksGraph(nestedGraphs: NavGraphBuilder.() -> Unit) {
    navigation(
        route = PARKS_GRAPH_ROUTE,
        startDestination = ROUTE_PARKS,
        deepLinks = listOf(navDeepLink { uriPattern = "$BASE_ROUTE$PARKS_GRAPH_ROUTE" }),
    ) {
        nestedGraphs()
    }
}
