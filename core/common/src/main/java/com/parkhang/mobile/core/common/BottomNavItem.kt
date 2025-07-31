package com.parkhang.mobile.core.common

/*
 * This is a sealed data class that represents a BottomNavItem.
 */
sealed class BottomNavItem(
    val title: String,
    val icon: Int,
    val route: String,
) {
    data object Parks : BottomNavItem(
        title = "Parks",
        icon = R.drawable.icons_navigation_parks_filled,
        route = PARKS_GRAPH_ROUTE,
    )

    data object Hangout : BottomNavItem(
        title = "Hangout",
        icon = R.drawable.icons_navigation_hangout,
        route = HANGOUT_GRAPH_ROUTE,
    )

    data object Profile : BottomNavItem(
        title = "Profile",
        icon = R.drawable.icons_navigation_user_outlined,
        route = PROFILE_GRAPH_ROUTE,
    )

    companion object {
        fun values() = listOf(Parks, Hangout, Profile)
    }
}

const val PARKS_GRAPH_ROUTE = "parksGraph"
const val HANGOUT_GRAPH_ROUTE = "hangoutGraph"
const val PROFILE_GRAPH_ROUTE = "profileGraph"
