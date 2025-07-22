package com.parkhang.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.parkhang.core.designsystem.theme.ParkHangTheme
import com.parkhang.mobile.navigation.NavigationNavHost
import com.parkhang.mobile.ui.HomeView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var startDestination by remember { mutableStateOf(PLACES_GRAPH_ROUTE) }
            var navController = rememberNavController()
            ParkHangTheme {
                HomeView(
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

const val PLACES_GRAPH_ROUTE = "parks"
