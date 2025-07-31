package com.parkhang.mobile.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.parkhang.mobile.core.common.BASE_ROUTE
import com.parkhang.mobile.core.common.HANGOUT_GRAPH_ROUTE
import com.parkhang.mobile.feature.view.HangoutRoute

fun NavController.navigateToHangoutGraph(navOptions: NavOptions? = null) {
    this.navigate(HANGOUT_GRAPH_ROUTE, navOptions)
}

fun NavGraphBuilder.hangoutGraph(nestedGraphs: NavGraphBuilder.() -> Unit) {
    navigation(
        route = HANGOUT_GRAPH_ROUTE,
        startDestination = HangoutRoute.BASE,
        deepLinks = listOf(navDeepLink { uriPattern = "$BASE_ROUTE$HANGOUT_GRAPH_ROUTE" }),
    ) {
        nestedGraphs()
    }
}
