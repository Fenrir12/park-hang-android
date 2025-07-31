package com.parkhang.mobile.feature.parks.entity

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class PinItem(
    val pinId: String,
    val pinPosition: LatLng,
    val pinSnippet: String,
    val pinZIndex: Float,
) : ClusterItem {
    override fun getPosition(): LatLng = pinPosition

    override fun getTitle(): String = pinId

    override fun getSnippet(): String = pinSnippet

    override fun getZIndex(): Float = pinZIndex

    constructor(
        pin: Pin,
        pinZIndex: Float,
    ) : this(
        pinPosition = LatLng(pin.latLong.latitude, pin.latLong.longitude),
        pinId = pin.id,
        pinSnippet = "",
        pinZIndex = pinZIndex,
    )
}
