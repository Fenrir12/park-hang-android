package com.parkhang.mobile.feature.view

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

fun NavController.navigateToHangout(
    parkId: String?,
    parkName: String?,
    navOptions: NavOptions? = null,
) {
    this.navigate(HangoutRoute.route(parkId = parkId, parkName = parkName), navOptions)
}

fun NavGraphBuilder.hangoutScreen() {
    composable(
        route = "${HangoutRoute.BASE}?${HangoutRoute.PARK_ID_ARG}={${HangoutRoute.PARK_ID_ARG}}&${HangoutRoute.PARK_NAME_ARG}={${HangoutRoute.PARK_NAME_ARG}}",
        arguments = listOf(
            navArgument(HangoutRoute.PARK_ID_ARG) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(HangoutRoute.PARK_NAME_ARG) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
        ),
    ) { backStackEntry ->
        val parkId = backStackEntry.arguments?.getString(HangoutRoute.PARK_ID_ARG)
        val parkName = backStackEntry.arguments?.getString(HangoutRoute.PARK_NAME_ARG)

        HangoutView(
            parkId = parkId,
            parkName = parkName,
        )
    }
}

object HangoutRoute {
    const val BASE = "hangout"
    const val PARK_ID_ARG = "parkId"
    const val PARK_NAME_ARG = "parkName"

    fun route(
        parkId: String? = null,
        parkName: String? = null,
    ): String {
        val params = buildList {
            parkId?.let { add("$PARK_ID_ARG=${Uri.encode(it)}") }
            parkName?.let { add("$PARK_NAME_ARG=${Uri.encode(it)}") }
        }

        return if (params.isNotEmpty()) {
            "$BASE?${params.joinToString("&")}"
        } else {
            BASE
        }
    }
}
