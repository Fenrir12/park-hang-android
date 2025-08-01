package com.parkhang.mobile.feature.parks.view

import android.util.Log
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
fun ParksView(
    onCheckIn: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ParksViewModel = hiltViewModel(),
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    val uiState by viewModel.uiStateFlow.collectAsState()
    val checkInState by viewModel.checkInStateFlow.collectAsState()

    val cameraPositionState =
        rememberCameraPositionState {
            position = NORTH_AMERICA_CAMERA_POSITION
        }

    LaunchedEffect(checkInState.checkInIsCompleted) {
        val checkIn = checkInState.checkIn
        if (checkIn != null && checkInState.checkInIsCompleted) {
            viewModel.onCheckInPerformed()
            onCheckIn(checkIn.currentParkView.id, checkIn.currentParkView.name)
        }
    }

    LaunchedEffect(checkInState.checkInFailure) {
        checkInState.checkInFailure?.let { failure ->
            // TODO: Handle check-in failure appropriately, e.g., show a snackbar or dialog
            Log.e("ParkView", "Check-in failed: $failure")
        }
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
                )
            },
        selectedPinId = uiState.selectedPinId,
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
        onRequestUserLocation = viewModel::getUserLocation,
        onPinItemSelected = viewModel::onSelectPin,
        onMapClicked = viewModel::onUnselectPin,
        coroutineScope = scope,
        modifier = modifier,
    )
}

@Composable
fun ParkScreen(
    pinList: List<PinItem>,
    selectedPinId: String?,
    parkItemList: List<ParkItem>,
    cameraPositionState: CameraPositionState,
    onRequestUserLocation: () -> Unit,
    onRequestNearbyParks: () -> Unit,
    onParkClicked: (String) -> Unit,
    onPinItemSelected: (String) -> Unit,
    onMapClicked: () -> Unit,
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
                selectedPinId = selectedPinId,
            )
        },
    ) {
        MapScreen(
            modifier =
                modifier
                    .fillMaxSize(),
            bottomPadding = SHEET_PEEK_HEIGHT,
            cameraPositionState = cameraPositionState,
            onMapClick = { _ ->
                onMapClicked()
            },
            onGetPinItemList = { pinList },
            onClusterClicked = { location ->
                coroutineScope.launch {
                    onClusterClicked(cameraPositionState, location)
                }
            },
            onClusterItemClicked = { pinItem ->
                onPinItemSelected(pinItem.pinId)
            },
            selectedPinId = selectedPinId,
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
                    onRequestUserLocation()
                }
            },
        )
    }
}

@Composable
fun ParkDrawerContent(
    parkItemList: List<ParkItem>,
    selectedPinId: String?,
    onParkClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(selectedPinId) {
        selectedPinId?.also {
            val index = parkItemList.indexOfFirst { it.id == selectedPinId }
            if (index != -1) {
                lazyListState.animateScrollToItem(index)
            }
        }
    }
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
                            onParkCardClicked = onParkClicked,
                            isSelected = (parkItem.id == selectedPinId),
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
        onRequestUserLocation = { },
        onPinItemSelected = { _ -> },
        onMapClicked = { },
        selectedPinId = null,
    )
}

val SHEET_PEEK_HEIGHT = 300.dp
