package com.parkhang.core.designsystem.icons

import com.parkhang.core.designsystem.R

object Icons {
    object Functional {
        val Warning = R.drawable.icons_warning
    }

    object Navigation {
        val Exit: Int = R.drawable.icons_navigation_exit

        object Back {
            val outlined: Int = R.drawable.icons_navigation_back_outlined
        }

        object Options {
            val outlined: Int = R.drawable.icons_chevron_down_outlined
        }

        object Email {
            val White: Int = R.drawable.icons_navigation_email_white_filled
        }

        object Profile {
            val White: Int = R.drawable.icons_navigation_user_outlined
        }

        object Dot {
            val Black: Int = R.drawable.icons_dot_small
        }
    }

    object Map {
        object Pin {
            val Green: Int = R.drawable.icons_map_pin_gradient_green
            val LightGreen: Int = R.drawable.icons_map_pin_gradient_light_green
            val Location: Int = R.drawable.icons_map_pin_location
        }

        object Cluster {
            val Circle: Int = R.drawable.icons_cluster_circle
        }

        object MyLocation {
            val Empty: Int = R.drawable.icons_location_uncentered
            val Full: Int = R.drawable.icons_location_centered
        }

        object Logo {
            val Google = R.drawable.icons_logo_google
        }
    }
}
