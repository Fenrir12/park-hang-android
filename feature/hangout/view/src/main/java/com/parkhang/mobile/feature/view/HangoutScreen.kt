package com.parkhang.mobile.feature.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.mobile.feature.hangout.entity.Hangout
import com.parkhang.mobile.feature.view.components.HangoutContentCheckedIn
import com.parkhang.mobile.feature.view.components.HangoutTopBar

@Composable
fun HangoutView(
    parkId: String?,
    parkName: String?,
    modifier: Modifier = Modifier,
    viewModel: HangoutViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiStateFlow.collectAsState()

    LaunchedEffect(parkId) {
        parkId?.let { id ->
            viewModel.getHangoutsForPark(id)
        }
    }
    HangoutScreen(
        parkName = parkName ?: "All Parks",
        hangoutList = uiState.hangoutList,
        modifier = modifier,
    )
}

@Composable
fun HangoutScreen(
    parkName: String,
    hangoutList: List<Hangout>,
    modifier: Modifier = Modifier,
) {
    HangoutContent(
        parkName = parkName,
        hangoutList = hangoutList,
        modifier = modifier,
    )
}

@Composable
fun HangoutContent(
    parkName: String?,
    hangoutList: List<Hangout>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = CustomColors.Primary.LightGreen,
            ).fillMaxSize(),
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = modifier
                .padding(
                    horizontal = Padding.Small.L,
                    vertical = Padding.Small.S,
                ).fillMaxSize(),
            topBar = {
                HangoutTopBar(
                    title = parkName,
                    onChangeHangoutView = { }, // TODO: Implement functionality to change hangout view between checked in park and all parks
                )
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(paddingValues = innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                HangoutContentCheckedIn(
                    modifier = Modifier
                        .matchParentSize(),
                    hangoutList = hangoutList,
                )
            }
        }
    }
}

@Preview
@Composable
fun HangoutViewPreview() {
    HangoutScreen(
        parkName = "Park Hangout",
        hangoutList = listOf(
            Hangout(
                ownerName = "John Doe",
                title = "Park Picnic",
                description = "Join us for a fun picnic at the park!",
                createdAt = "2023-10-01T12:00:00Z",
            ),
        ),
    )
}
