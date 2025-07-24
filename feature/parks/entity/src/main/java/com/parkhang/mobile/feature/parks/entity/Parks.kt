package com.parkhang.mobile.feature.parks.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Park(
    private val properties: Properties,
    private val geometry: Geometry,
    @SerialName("_id") private val internalId: String,
    private val type: String,
    val id: String,
) {
    val name: String?
        get() = properties.name
    val location: LatLong
        get() = LatLong(geometry.coordinates[1], geometry.coordinates[0])
}

@Serializable
data class Properties(
    @SerialName("@id") val osmId: String,
    val leisure: String,
    val name: String? = null,
    val website: String? = null,
    @SerialName("@geometry") val geometryType: String? = null,
    @SerialName("addr:city") val city: String? = null,
    @SerialName("addr:housenumber") val houseNumber: String? = null,
    @SerialName("addr:postcode") val postcode: String? = null,
    @SerialName("addr:province") val province: String? = null,
    @SerialName("addr:street") val street: String? = null,
    val image: String? = null,
    val operator: String? = null,
    @SerialName("operator:type") val operatorType: String? = null,
    @SerialName("operator:wikidata") val operatorWikidata: String? = null,
    @SerialName("internet_access") val internetAccess: String? = null,
    @SerialName("internet_access:fee") val internetAccessFee: String? = null,
    @SerialName("internet_access:operator") val internetAccessOperator: String? = null,
    @SerialName("internet_access:password") val internetAccessPassword: String? = null,
    @SerialName("internet_access:ssid") val internetAccessSsid: String? = null,
    @SerialName("opening_hours") val openingHours: String? = null,
    val wikidata: String? = null,
)

@Serializable
data class Geometry(
    val type: String,
    val coordinates: List<Double>,
)
