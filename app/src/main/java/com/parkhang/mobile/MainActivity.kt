package com.parkhang.mobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.parkhang.core.designsystem.theme.ParkHangTheme
import com.parkhang.mobile.core.checkin.statemachine.CheckInStateMachine
import com.parkhang.mobile.core.common.BottomNavItem
import com.parkhang.mobile.core.common.HANGOUT_GRAPH_ROUTE
import com.parkhang.mobile.core.common.PARKS_GRAPH_ROUTE
import com.parkhang.mobile.core.common.PROFILE_GRAPH_ROUTE
import com.parkhang.mobile.core.userprofile.datasource.UserProfileRepository
import com.parkhang.mobile.navigation.NavigationNavHost
import com.parkhang.mobile.navigation.asTopLevelDestination
import com.parkhang.mobile.navigation.graphs.navigateToHangoutGraph
import com.parkhang.mobile.navigation.graphs.navigateToParksGraph
import com.parkhang.mobile.navigation.graphs.navigateToProfileGraph
import com.parkhang.mobile.ui.HomeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userProfileRepository: UserProfileRepository

    @Inject
    lateinit var checkInStateMachine: CheckInStateMachine

    @SuppressLint("RestrictedApi", "StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var startDestination by remember { mutableStateOf(PARKS_GRAPH_ROUTE) }
            var navController = rememberNavController()
            val currentBackStack by navController.currentBackStackEntryAsState()
            val currentDestination = currentBackStack?.destination

            Log.v("TEST", "Current backstack entry: ${navController.currentBackStack.value.map { it.destination.route }}")

            ParkHangTheme {
                HomeView(
                    currentDestination = currentDestination?.asTopLevelDestination(),
                    onNavigateToMainDestination = { destination ->
                        navigateToTopLevelDestination(
                            navController = navController,
                            item = destination,
                        )
                    },
                    navHost = { modifier ->
                        NavigationNavHost(
                            startDestination = startDestination,
                            navHostController = navController,
                            modifier =
                                modifier
                                    .fillMaxSize(),
                        )
                    },
                )
            }
        }
    }
}

private fun navigateToTopLevelDestination(
    navController: NavHostController,
    item: BottomNavItem,
) {
    val topLevelNavOptions = navOptions(navController)
    when (item.route) {
        PARKS_GRAPH_ROUTE -> navController.navigateToParksGraph(topLevelNavOptions)
        HANGOUT_GRAPH_ROUTE -> navController.navigateToHangoutGraph(topLevelNavOptions)
        PROFILE_GRAPH_ROUTE -> navController.navigateToProfileGraph(topLevelNavOptions)
    }
}

private fun navOptions(navController: NavController) = navOptions {
    popUpTo(navController.graph.findStartDestination().id) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}
