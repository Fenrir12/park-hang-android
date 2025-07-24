package com.parkhang.mobile.feature.parks.view

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.CameraPositionState
import kotlin.coroutines.cancellation.CancellationException

internal suspend fun onMoveToUserLocation(
    camera: CameraPositionState,
    location: LatLng,
    shouldAnimate: Boolean,
) {
    moveToLocationWithZoom(
        camera = camera,
        location = location,
        zoomLevel = INITIAL_ZOOM,
        shouldAnimate = shouldAnimate,
    )
}

internal val onClusterClicked: suspend (camera: CameraPositionState, location: LatLng) -> Unit = { camera, location ->
    val newZoom = camera.position.zoom + ON_CLICK_ZOOM
    moveToLocationWithZoom(
        camera = camera,
        location = location,
        zoomLevel = newZoom,
        shouldAnimate = true,
    )
}

internal val onGetVisibleCameraRadius: (bounds: CameraPositionState) -> Int = { cameraState ->
    cameraState.projection?.visibleRegion?.latLngBounds?.let { bounds ->
        val northLatLng = LatLng(bounds.northeast.latitude, bounds.center.longitude)
        val southLatLng = LatLng(bounds.southwest.latitude, bounds.center.longitude)

        SphericalUtil.computeDistanceBetween(northLatLng, southLatLng).toInt()
    } ?: 1_000
}

private suspend fun moveToLocationWithZoom(
    camera: CameraPositionState,
    location: LatLng?,
    zoomLevel: Float,
    shouldAnimate: Boolean = true,
) {
    try {
        location?.let { loc ->
            val cameraUpdateFullScreen = CameraUpdateFactory.newLatLngZoom(location, zoomLevel)

            val targetUpdate = cameraUpdateFullScreen

            if (shouldAnimate) {
                camera.animate(targetUpdate)
            } else {
                camera.move(targetUpdate)
            }
        }
    } catch (exception: CancellationException) {
        throw exception
    }
}

internal const val INITIAL_ZOOM = 15f
internal const val NO_LOCATION_ZOOM = 3f
internal val NORTH_AMERICA_CAMERA_POSITION =
    CameraPosition.fromLatLngZoom(
        LatLng(44.772456, -92.658507),
        NO_LOCATION_ZOOM,
    )
internal const val ON_CLICK_ZOOM = 1f
internal const val CENTERED_ON_POSITION_DISTANCE = 500
