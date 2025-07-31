package com.parkhang.mobile.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.parkhang.mobile.core.common.BASE_ROUTE
import com.parkhang.mobile.core.common.PROFILE_GRAPH_ROUTE
import com.parkhang.mobile.feature.profile.view.ROUTE_PROFILE

fun NavController.navigateToProfileGraph(navOptions: NavOptions? = null) {
    this.navigate(PROFILE_GRAPH_ROUTE, navOptions)
}

fun NavGraphBuilder.profileGraph(nestedGraphs: NavGraphBuilder.() -> Unit) {
    navigation(
        route = PROFILE_GRAPH_ROUTE,
        startDestination = ROUTE_PROFILE,
        deepLinks = listOf(navDeepLink { uriPattern = "$BASE_ROUTE$PROFILE_GRAPH_ROUTE" }),
    ) {
        nestedGraphs()
    }
}
