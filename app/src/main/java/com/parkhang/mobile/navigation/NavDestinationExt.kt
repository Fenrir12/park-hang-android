package com.parkhang.mobile.navigation

import androidx.navigation.NavDestination
import com.parkhang.mobile.core.common.BottomNavItem
import kotlin.collections.firstOrNull

fun NavDestination?.asTopLevelDestination(): BottomNavItem? = this?.parent?.asMainRoute() ?: this?.asMainRoute()

fun NavDestination.asMainRoute(): BottomNavItem? = BottomNavItem.values().firstOrNull { it.route == route }
