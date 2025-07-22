package com.parkhang.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.parkhang.core.designsystem.theme.CustomColors.Transparencies

@Composable
fun HomeView(
    navHost: @Composable (modifier: Modifier) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(color = Transparencies.Black)
                .fillMaxSize(),
    ) {
        navHost(
            Modifier
                .fillMaxSize(),
        )
    }
}
