package com.parkhang.mobile.framework.network.client.model

import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.URLBuilder

/**
 * NetworkRequestBuilder to adapt the request parameters
 */
class NetworkRequestBuilder : HttpMessageBuilder {
    override val headers: HeadersBuilder = HeadersBuilder()
    var baseUrl: URLBuilder = URLBuilder()
}
