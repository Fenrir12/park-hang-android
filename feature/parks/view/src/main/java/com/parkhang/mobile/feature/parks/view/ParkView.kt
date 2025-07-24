package com.parkhang.mobile.feature.parks.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.parkhang.core.designsystem.icons.Icons
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle
import com.parkhang.mobile.feature.parks.entity.PinItem
import com.parkhang.mobile.feature.parks.view.map.MapScreen
import com.parkhang.mobile.feature.parks.view.onGetVisibleCameraRadius
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ParkView(
    modifier: Modifier = Modifier,
    viewModel: ParksViewModel = hiltViewModel(),
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    val uiState by viewModel.uiStateFlow.collectAsState()
    val cameraPositionState =
        rememberCameraPositionState {
            position = NORTH_AMERICA_CAMERA_POSITION
        }
    ParkScreen(
        pinList =
            uiState.pinList.map { pin ->
                PinItem(
                    pin = pin,
                    pinZIndex = 1f,
                    iconId = Icons.Map.Pin.Green,
                )
            },
        parksQuantity = uiState.parkList.size.toString(),
        cameraPositionState = cameraPositionState,
        onRequestNearbyParks = {
            viewModel.fetchParks(onGetVisibleCameraRadius(cameraPositionState))
        },
        onParkButtonClicked = {
            viewModel.fetchParks(onGetVisibleCameraRadius(cameraPositionState))
        },
        currentLocation =
            uiState.userLocation?.let {
                LatLng(
                    it.latitude,
                    it.longitude,
                )
            },
        coroutineScope = scope,
        modifier = modifier,
    )
}

@Composable
fun ParkScreen(
    pinList: List<PinItem>,
    cameraPositionState: CameraPositionState,
    onParkButtonClicked: () -> Unit,
    onRequestNearbyParks: () -> Unit,
    parksQuantity: String,
    currentLocation: LatLng? = null,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
) {
    val bottomSheetState =
        rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
        )
    val scaffoldState =
        rememberBottomSheetScaffoldState(
            bottomSheetState = bottomSheetState,
        )
    var moveAnimationCancelled by rememberSaveable { mutableStateOf<LatLng?>(null) }
    var cancelNextMapAnimation by remember { mutableStateOf(false) }
    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetContent = {
            ParkDrawerContent(
                modifier =
                    Modifier
                        .fillMaxSize(),
                onParkButtonClicked = onParkButtonClicked,
                parksQuantity = parksQuantity,
            )
        },
    ) {
        MapScreen(
            modifier =
                modifier
                    .fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { }, // TODO: Implement with pin selection feature
            onGetPinItemList = { pinList },
            onClusterClicked = { location ->
                coroutineScope.launch {
                    onClusterClicked(cameraPositionState, location)
                }
            },
            onClusterItemClicked = { }, // TODO: Implement with pin selection feature
            onMoveToInitialLocation = { location ->
                coroutineScope.launch {
                    try {
                        if (!cancelNextMapAnimation) {
                            onMoveToUserLocation(
                                camera = cameraPositionState,
                                location = location,
                                shouldAnimate = true,
                            )
                        } else {
                            cancelNextMapAnimation = false
                        }
                    } catch (e: CancellationException) {
                        moveAnimationCancelled = location
                    }
                }
            },
            onRequestNearbyParks = onRequestNearbyParks,
            currentLocation = currentLocation,
            onMyLocationClicked = {
                coroutineScope.launch {
                    currentLocation?.let { location ->
                        onMoveToUserLocation(
                            camera = cameraPositionState,
                            location = location,
                            shouldAnimate = true,
                        )
                    }
                }
            },
        )
    }
}

@Composable
fun ParkDrawerContent(
    onParkButtonClicked: () -> Unit,
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
            Button(
                onClick = onParkButtonClicked,
            ) {
                Text(
                    text = "Fetch Parks",
                    style = CustomTextStyle.Body1.copy(color = CustomColors.Transparencies.White),
                )
            }
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
    val cameraPositionState =
        rememberCameraPositionState {
            position = NORTH_AMERICA_CAMERA_POSITION
        }
    ParkScreen(
        coroutineScope = rememberCoroutineScope(),
        parksQuantity = "10",
        cameraPositionState = cameraPositionState,
        onParkButtonClicked = { },
        onRequestNearbyParks = { },
        pinList = emptyList(),
    )
}
