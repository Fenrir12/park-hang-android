package com.parkhang.mobile.feature.parks.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.parkhang.core.designsystem.icons.Icons
import com.parkhang.core.designsystem.layout.Layout
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle
import com.parkhang.mobile.feature.parks.entity.ParkItem
import com.parkhang.mobile.feature.parks.entity.PinItem
import com.parkhang.mobile.feature.parks.view.components.CustomDragHandle
import com.parkhang.mobile.feature.parks.view.components.ParkItemContent
import com.parkhang.mobile.feature.parks.view.components.RequestLocationPermissions
import com.parkhang.mobile.feature.parks.view.map.MapScreen
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

    LaunchedEffect(uiState.pinList) {
        if (uiState.pinList.isNotEmpty()) {
            viewModel.fetchParkByIds(
                parkIdList = uiState.pinList.map { it.id },
            )
        }
    }

    RequestLocationPermissions(
        onGranted = viewModel::getUserLocation,
    )

    ParkScreen(
        pinList =
            uiState.pinList.map { pin ->
                PinItem(
                    pin = pin,
                    pinZIndex = 1f,
                    iconId = Icons.Map.Pin.Green,
                )
            },
        parkItemList = uiState.parkItemList,
        cameraPositionState = cameraPositionState,
        onRequestNearbyParks = {
            viewModel.fetchPins(
                cameraCenter = cameraPositionState.position.target,
                searchRadius = onGetVisibleCameraRadius(cameraPositionState),
            )
        },
        onParkClicked = viewModel::getParkById,
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
    parkItemList: List<ParkItem>,
    cameraPositionState: CameraPositionState,
    onRequestNearbyParks: () -> Unit,
    onParkClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    currentLocation: LatLng? = null,
    coroutineScope: CoroutineScope,
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
        sheetDragHandle = { CustomDragHandle() },
        sheetContainerColor = CustomColors.Primary.LightGreen,
        sheetContentColor = CustomColors.Primary.LightGreen,
        sheetPeekHeight = SHEET_PEEK_HEIGHT,
        sheetContent = {
            ParkDrawerContent(
                modifier =
                    Modifier
                        .fillMaxSize(),
                parkItemList = parkItemList,
                onParkClicked = onParkClicked,
            )
        },
    ) {
        MapScreen(
            modifier =
                modifier
                    .fillMaxSize(),
            bottomPadding = SHEET_PEEK_HEIGHT,
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
    parkItemList: List<ParkItem>,
    onParkClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()
    Box(
        modifier =
            modifier
                .background(CustomColors.Primary.LightGreen)
                .fillMaxSize()
                .padding(horizontal = Padding.Small.L),
    ) {
        Column(
            modifier =
                Modifier
                    .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Nearby Parks",
                style = CustomTextStyle.Heading2.copy(color = CustomColors.Transparencies.White),
                modifier =
                    Modifier
                        .wrapContentSize(),
            )
            Spacer(
                modifier =
                    Modifier
                        .height(Layout.Spacing.Medium.M),
            )
            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(Padding.Small.M),
                contentPadding =
                    PaddingValues(
                        bottom = Layout.Spacing.Medium.M,
                    ),
            ) {
                itemsIndexed(parkItemList) { _, parkItem ->
                    parkItem.name?.let { name ->
                        ParkItemContent(
                            parkId = parkItem.id,
                            parkName = name,
                            parkDistance = parkItem.distanceFromUser,
                            onParkCardClicked = onParkClicked, // TODO: Implement park click action
                        )
                    }
                }
            }
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
        cameraPositionState = cameraPositionState,
        onRequestNearbyParks = { },
        pinList = emptyList(),
        parkItemList =
            listOf(
                ParkItem(
                    id = "1",
                    name = "Central Park",
                    distanceFromUser = 50,
                ),
                ParkItem(
                    id = "2",
                    name = "Golden Gate Park",
                    distanceFromUser = 500,
                ),
                ParkItem(
                    id = "3",
                    name = "Hyde Park",
                    distanceFromUser = 1500,
                ),
            ),
        onParkClicked = { _ -> },
    )
}

val SHEET_PEEK_HEIGHT = 300.dp
