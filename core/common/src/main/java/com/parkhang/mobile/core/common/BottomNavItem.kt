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
        route = PARKS_ROUTE,
    )

    data object Profile : BottomNavItem(
        title = "Profile",
        icon = R.drawable.icons_navigation_user_outlined,
        route = PROFILE_ROUTE,
    )

    companion object {
        fun values() = listOf(Parks, Profile)
    }
}

const val PARKS_ROUTE = "parks"
const val PROFILE_ROUTE = "profile"
