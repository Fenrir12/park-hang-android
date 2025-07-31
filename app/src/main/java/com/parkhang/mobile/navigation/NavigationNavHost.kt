package com.parkhang.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.parkhang.mobile.core.common.HANGOUT_GRAPH_ROUTE
import com.parkhang.mobile.feature.parks.view.parksScreen
import com.parkhang.mobile.feature.profile.view.profileScreen
import com.parkhang.mobile.feature.view.HangoutRoute
import com.parkhang.mobile.feature.view.hangoutScreen
import com.parkhang.mobile.feature.view.navigateToHangout
import com.parkhang.mobile.navigation.graphs.hangoutGraph
import com.parkhang.mobile.navigation.graphs.parksGraph
import com.parkhang.mobile.navigation.graphs.profileGraph

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
        parksGraph {
            parksScreen(
                onCheckIn = { parkId, parkName ->
                    if (navHostController.currentDestination?.route?.contains(HangoutRoute.BASE) == false) {
                        navHostController.clearBackStack(HANGOUT_GRAPH_ROUTE)
                    }
                    navHostController.navigateToHangout(
                        parkId = parkId,
                        parkName = parkName,
                        navOptions = navOptions {
                            popUpTo(navHostController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        },
                    )
                },
            )
        }
        hangoutGraph {
            hangoutScreen()
        }
        profileGraph {
            profileScreen()
        }
    }
}
