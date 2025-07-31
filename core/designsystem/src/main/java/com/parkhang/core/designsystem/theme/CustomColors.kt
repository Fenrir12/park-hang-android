package com.parkhang.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * [CustomColors] containing the custom color values.
 */
object CustomColors {
    object Border {
        val StrokeTint = Neutrals.White30
    }

    object Transparencies {
        val Black = Color(0xFF000000)
        val Gray = Color(0xFF888888)
        val DarkGray = Color(0xFF222222)
        val MediumGray = Color(0xFF333333)
        val LightGray = Color(0xFF777777)
        val White = Color(0xFFFFFFFF)
    }

    object Neutrals {
        val Black60 = Color(0x99000000)
        val Black30 = Color(0x4D000000)
        val Black15 = Color(0x26000000)
        val White5 = Color(0x0DFFFFFF)
        val White10 = Color(0x1AFFFFFF)
        val White12 = Color(0x1FFFFFFF)
        val White13 = Color(0x21FFFFFF)
        val White18 = Color(0x2EFFFFFF)
        val White30 = Color(0x4DFFFFFF)
        val White40 = Color(0x66FFFFFF)
        val White50 = Color(0x80FFFFFF)
        val White60 = Color(0x99FFFFFF)
        val White85 = Color(0xD9FFFFFF)
    }

    object Primary {
        val DarkGreen = Color(0xFF1B5E20)
        val Green = Color(0xFF4CAF50)
        val LightGreen = Color(0xFF81C784)
        val VeryLightGreen = Color(0xFFCCF0D1)
    }

    object Accent {
        val DarkYellow = Color(0xFFDF8321)
        val Yellow = Color(0xFFFFB74D)
        val ClearYellow = Color(0xFFFFFFE0)
        val Red = Color(0xFFD72638)
        val RedLight = Color(0xFFF28B82)
    }

    object Status {
        val Error = Color(0xFFD72638)
        val ErrorBG = Color(0xFFcc3260)
    }
}
