package com.parkhang.mobile.framework.network.client.config

import com.parkhang.mobile.framework.network.client.model.NetworkRequestBuilder
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom

/**
 * Support functions
 *
 */
fun NetworkRequestBuilder.addBearerToken(token: String): Unit = header(HttpHeaders.Authorization, "Bearer $token")

fun NetworkRequestBuilder.addBaseUrl(url: String) {
    baseUrl = URLBuilder().takeFrom(url)
}

fun NetworkRequestBuilder.addUserCountryHeader(userCountry: String): Unit = header("userCountry", userCountry)

fun NetworkRequestBuilder.addAnonymousHeader(): Unit = header("Anonymous", true)

fun NetworkRequestBuilder.addInstallAppIdHeader(installAppId: String): Unit = header("X-App-Install-ID", installAppId)

fun NetworkRequestBuilder.addUserAnonymousIdHeader(anonymousId: String): Unit = header("X-Anonymous-ID", anonymousId)

fun addRequestParams(block: NetworkRequestBuilder.() -> Unit): NetworkRequestBuilder.() -> Unit = { block() }
