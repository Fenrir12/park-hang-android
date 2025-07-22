package com.parkhang.mobile.feature.parks.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle

@Composable
fun ParkView(
    modifier: Modifier = Modifier,
    viewModel: ParksViewModel = hiltViewModel(),
) {
    val parksQuantity by remember { mutableStateOf(viewModel.parksQuantity) }
    ParkScreen(
        parksQuantity = parksQuantity,
        modifier = modifier,
    )
}

@Composable
fun ParkScreen(
    modifier: Modifier = Modifier,
    parksQuantity: String? = null,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Park View",
                style = CustomTextStyle.Heading2.copy(color = CustomColors.Transparencies.White),
                modifier =
                    Modifier
                        .wrapContentSize(),
            )
            Text(
                text = "Parks fetched: ${parksQuantity ?: "..."}",
                style = CustomTextStyle.Heading2.copy(color = CustomColors.Transparencies.White),
                modifier =
                    Modifier
                        .wrapContentSize(),
            )
        }
    }
}

@Preview
@Composable
fun ParkScreenPreview() {
    ParkScreen(
        parksQuantity = "10",
    )
}
