package com.parkhang.mobile.feature.parks.view.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.clustering.rememberClusterRenderer
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.ktx.utils.sphericalDistance
import com.parkhang.core.designsystem.icons.Icons
import com.parkhang.core.designsystem.layout.CornerRadius
import com.parkhang.core.designsystem.layout.Layout.Spacing.Medium
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.map.MapStyle
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.theme.CustomColors.Neutrals.Black60
import com.parkhang.core.designsystem.theme.CustomColors.Transparencies.White
import com.parkhang.core.designsystem.theme.Opacity
import com.parkhang.mobile.core.designsystem.components.conditional
import com.parkhang.mobile.feature.parks.entity.PinItem
import com.parkhang.mobile.feature.parks.view.CENTERED_ON_POSITION_DISTANCE
import com.parkhang.mobile.feature.parks.view.NORTH_AMERICA_CAMERA_POSITION
import com.parkhang.mobile.feature.parks.view.SHEET_PEEK_HEIGHT
import com.parkhang.mobile.feature.parks.view.map.components.ClusterCircleContent
import com.parkhang.mobile.feature.parks.view.map.components.MapMarker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration

@Composable
fun MapScreen(
    cameraPositionState: CameraPositionState,
    onMapClick: (LatLng) -> Unit,
    onGetPinItemList: () -> List<PinItem>,
    onClusterClicked: (LatLng) -> Unit,
    onClusterItemClicked: (PinItem) -> Unit,
    onMoveToInitialLocation: (position: LatLng) -> Unit,
    onRequestNearbyParks: () -> Unit,
    onMyLocationClicked: () -> Unit,
    selectedPinId: String?,
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    currentLocation: LatLng? = null,
    bottomPadding: Dp = BottomSheetDefaults.SheetPeekHeight,
) {
    var lastPosition by rememberSaveable { mutableStateOf(LatLng(0.0, 0.0)) }
    LaunchedEffect(currentLocation) {
        if (lastPosition != currentLocation) {
            currentLocation?.let { location ->
                onMoveToInitialLocation(location)
                lastPosition = currentLocation
            }
        }
    }
    MapContent(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onMapClick = onMapClick,
        onGetPinItemList = onGetPinItemList,
        onClusterClicked = onClusterClicked,
        onClusterItemClicked = onClusterItemClicked,
        onRequestNearbyParks = onRequestNearbyParks,
        coroutineScope = coroutineScope,
        currentLocation = currentLocation,
        onMyLocationClicked = onMyLocationClicked,
        bottomPadding = bottomPadding,
        selectedPinId = selectedPinId,
    )
}

@Composable
fun MapContent(
    cameraPositionState: CameraPositionState,
    onMapClick: (LatLng) -> Unit,
    onGetPinItemList: () -> List<PinItem>,
    onClusterClicked: (LatLng) -> Unit,
    onClusterItemClicked: (PinItem) -> Unit,
    onRequestNearbyParks: () -> Unit,
    onMyLocationClicked: () -> Unit,
    modifier: Modifier = Modifier,
    bottomPadding: Dp = 0.dp,
    selectedPinId: String?,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    currentLocation: LatLng? = null,
) {
    var isMapReady by remember { mutableStateOf(false) }

    LaunchedEffect(cameraPositionState.position, cameraPositionState.isMoving, isMapReady) {
        if (!cameraPositionState.isMoving && isMapReady && currentLocation != null) {
            cameraPositionState.projection?.visibleRegion?.latLngBounds?.let { _ ->
                onRequestNearbyParks()
            }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        GoogleMap(
            modifier =
                modifier
                    .padding(bottom = SHEET_PEEK_HEIGHT - Padding.Medium.L)
                    .fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties =
                MapProperties(
                    mapStyleOptions =
                        MapStyleOptions
                            .loadRawResourceStyle(
                                LocalContext.current,
                                MapStyle.RetroStyleResId,
                            ),
                ),
            uiSettings =
                MapUiSettings(
                    compassEnabled = false,
                    zoomControlsEnabled = false,
                    mapToolbarEnabled = false,
                    scrollGesturesEnabledDuringRotateOrZoom = false,
                ),
            googleMapOptionsFactory = {
                GoogleMapOptions()
                    .apply {
                        this.backgroundColor(CustomColors.Transparencies.Black.toArgb())
                    }
            },
            onMapClick = onMapClick,
            onMyLocationButtonClick = { false },
            contentPadding = PaddingValues(), // Do not add padding here, you will introduce a map bug on navigation
        ) {
            MapEffect(cameraPositionState) { map ->
                map.setOnMapLoadedCallback {
                    cameraPositionState.projection?.visibleRegion?.latLngBounds?.let { _ ->
                        isMapReady = true
                    }
                }
            }
            CustomRendererClustering(
                items = onGetPinItemList(),
                onClusterClicked = onClusterClicked,
                onClusterItemClicked = onClusterItemClicked,
                coroutineScope = coroutineScope,
                selectedPinId = selectedPinId,
            )
            currentLocation?.let { location ->
                MapMarker(position = location, iconResourceId = Icons.Map.Pin.Location)
            }
        }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = NO_ERROR_BOTTOM_PADDING + bottomPadding)
                    .wrapContentHeight(),
        ) {
            Image(
                painter = painterResource(id = Icons.Map.Logo.Google),
                contentDescription = "Google Logo",
                modifier =
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(
                            start = Padding.Small.M,
                            bottom = Padding.Small.S,
                        ),
            )
            UserLocationButton(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            end = Padding.Small.L,
                            bottom = Padding.Small.M,
                        ),
                onClick = onMyLocationClicked,
            ) {
                fun shouldShowFilledUserLocationButton(): Boolean {
                    val cameraPosition = cameraPositionState.position.target
                    return when {
                        currentLocation == null -> false

                        cameraPosition.sphericalDistance(currentLocation) <= CENTERED_ON_POSITION_DISTANCE -> true
                        else -> false
                    }
                }

                if (shouldShowFilledUserLocationButton()) {
                    Icon(
                        painter = painterResource(id = Icons.Map.MyLocation.Full),
                        tint = Color.Unspecified,
                        contentDescription = "Center on user current location",
                    )
                } else {
                    Icon(
                        painter = painterResource(id = Icons.Map.MyLocation.Empty),
                        tint = Color.Unspecified,
                        contentDescription = "Center on user current location",
                    )
                }
            }
        }
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun CustomRendererClustering(
    items: List<PinItem>,
    onClusterClicked: (position: LatLng) -> Unit,
    onClusterItemClicked: (pinItem: PinItem) -> Unit,
    coroutineScope: CoroutineScope,
    selectedPinId: String?,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val clusterManager = rememberClusterManager<PinItem>()

    clusterManager?.setAlgorithm(
        NonHierarchicalViewBasedAlgorithm(
            screenWidth.value.toInt(),
            screenHeight.value.toInt(),
        ),
    )
    clusterManager?.algorithm?.maxDistanceBetweenClusteredItems = MAX_CLUSTER_DISTANCE_IN_DP
    val renderer =
        rememberClusterRenderer(
            clusterContent = { cluster ->
                val upsampledClusterSize: Int = cluster.size

                val clusterSizeText =
                    if (upsampledClusterSize >= 1000) {
                        "${upsampledClusterSize / 1000}k"
                    } else {
                        "$upsampledClusterSize"
                    }
                ClusterCircleContent(
                    clusterSizeText = clusterSizeText,
                    clusterSize = upsampledClusterSize,
                )
            },
            clusterItemContent = { pinItem ->
                PinMarker(
                    isSelectedPin = pinItem.pinId == selectedPinId,
                )
            },
            clusterManager = clusterManager,
        )
    (renderer as? DefaultClusterRenderer)?.minClusterSize = MIN_CLUSTER_SIZE
    SideEffect {
        clusterManager ?: return@SideEffect
        clusterManager.setOnClusterClickListener { cluster ->
            onClusterClicked(cluster.position)
            true
        }
        clusterManager.setOnClusterItemClickListener { pinItem ->
            renderer?.setAnimation(false)
            onClusterItemClicked(pinItem)
            // TODO Technical debt: this can be removed once this is fixed: https://github.com/googlemaps/android-maps-compose/issues/311
            coroutineScope.launch {
                delay(Duration.ofMillis(CLUSTER_ANIMATION_DELAY_IN_MILLIS))
                renderer?.setAnimation(true)
            }
            true
        }
    }
    SideEffect {
        if (clusterManager?.renderer != renderer) {
            clusterManager?.renderer = renderer ?: return@SideEffect
        }
    }

    if (clusterManager != null) {
        Clustering(
            items = items,
            clusterManager = clusterManager,
        )
    }
}

@Composable
private fun PinMarker(
    modifier: Modifier = Modifier,
    isSelectedPin: Boolean = false,
) {
    Image(
        painterResource(
            id = if (isSelectedPin) Icons.Map.Pin.LightGreen else Icons.Map.Pin.Green,
        ),
        contentDescription = "Selected map pin for a place with a jukebox",
        modifier = modifier
            .conditional(isSelectedPin) {
                this.padding(
                    all = Padding.Small.S,
                )
            }.scale(if (isSelectedPin) 1.3f else 1f)
            .zIndex(USER_MARKER_Z_INDEX),
    )
}

@Composable
fun UserLocationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Button(
        onClick = onClick,
        modifier =
            modifier
                .height(Medium.L)
                .defaultMinSize(Medium.L)
                .alpha(if (isPressed) Opacity.FIFTY_PERCENT else Opacity.OPAQUE),
        shape = CornerRadius.Rounded,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = Black60,
                contentColor = White,
            ),
        contentPadding =
            PaddingValues(
                all = Padding.Small.M,
            ),
        interactionSource = interactionSource,
    ) {
        icon()
    }
}

@Preview
@Composable
private fun MapScreenPreview() {
    val cameraPositionState =
        rememberCameraPositionState {
            position = NORTH_AMERICA_CAMERA_POSITION
        }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CustomColors.Transparencies.Black,
    ) {
        MapContent(
            cameraPositionState = cameraPositionState,
            onMapClick = {},
            onGetPinItemList = { emptyList() },
            onClusterClicked = {},
            onClusterItemClicked = {},
            onRequestNearbyParks = {},
            coroutineScope = rememberCoroutineScope(),
            currentLocation = LatLng(0.0, 0.0),
            onMyLocationClicked = {},
            selectedPinId = null,
        )
    }
}

private const val CLUSTER_ANIMATION_DELAY_IN_MILLIS = 400L
private const val MIN_CLUSTER_SIZE = 7
private const val MAX_CLUSTER_DISTANCE_IN_DP = 85
private const val USER_MARKER_Z_INDEX = 3f
private val NO_ERROR_BOTTOM_PADDING = Padding.Medium.M
