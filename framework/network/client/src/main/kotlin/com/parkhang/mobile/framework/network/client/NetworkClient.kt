package com.parkhang.mobile.framework.network.client

import com.parkhang.mobile.framework.network.client.model.ApiResponse
import com.parkhang.mobile.framework.network.client.model.NetworkAdaptRequestFactory
import com.parkhang.mobile.framework.network.client.model.NetworkRequestBuilder
import com.parkhang.mobile.framework.network.client.model.NetworkRequestType
import com.parkhang.mobile.framework.network.client.model.NetworkRequestType.Anonymous
import com.parkhang.mobile.framework.network.client.model.NetworkRequestType.Authenticated
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.prepareRequest
import io.ktor.client.plugins.timeout
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpStatement
import io.ktor.client.utils.EmptyContent
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import javax.inject.Inject

class NetworkClient
    @Inject
    constructor(
        val client: HttpClient,
        val adaptRequest: () -> NetworkAdaptRequestFactory,
    ) {
        suspend inline fun <reified T : Any, reified R : Any> execute(
            resource: R,
            method: HttpMethod = HttpMethod.Get,
            requestedTimeoutMillis: Long? = null,
            body: Any = EmptyContent,
            vararg headers: Pair<String, List<String>> = emptyArray(),
            requestType: NetworkRequestType = Anonymous,
        ): ApiResponse<T> {
            val requestParameters =
                try {
                    val adaptRequest = adaptRequest()
                    when (requestType) {
                        Anonymous -> adaptRequest.anonymous()
                        Authenticated -> adaptRequest.authenticated()
                    }
                } catch (exception: Exception) {
                    return ApiResponse.Error(exception.message.orEmpty())
                }

            val request =
                buildRequest(
                    resource = resource,
                    requestParams = requestParameters,
                    requestMethod = method,
                    requestBody = body,
                    requestTimeoutMilliseconds = requestedTimeoutMillis ?: RESPONSE_TIMEOUT_DEFAULT,
                    headers = headers,
                )

            return executeRequest<T>(request)
        }

        /**
         * Build request and return a function that returns a HttpStatement
         *
         * @param resource: Resource classes <R>
         * @param requestParams: Embedded parameters using NetworkRequestBuilder
         * @param requestMethod: HttpMethod method. Default method is HttpMethod.Get
         * @param headers: Request headers. Default is an empty array
         * @return the () -> HttpStatement
         */
        suspend inline fun <reified R : Any> buildRequest(
            resource: R,
            crossinline requestParams: NetworkRequestBuilder.() -> Unit,
            requestMethod: HttpMethod = HttpMethod.Get,
            requestTimeoutMilliseconds: Long,
            requestBody: Any = EmptyContent,
            vararg headers: Pair<String, List<String>> = emptyArray(),
        ): suspend () -> HttpStatement =
            {
                val params = NetworkRequestBuilder().apply(requestParams)
                client.prepareRequest(resource) {
                    method = requestMethod

                    this.headers.appendAll(params.headers.build())
                    this.headers.appendAll(headersOf(*headers))

                    with(params.baseUrl) {
                        url.protocol = protocol
                        url.host = host
                        url.port = port
                    }

                    setBody(requestBody)

                    timeout {
                        requestTimeoutMillis = requestTimeoutMilliseconds
                    }
                }
            }

        /**
         * Execute the request and return [ApiResponse]
         *
         * @param request: function that returns a HttpStatement
         * @return the [ApiResponse] serialized by <T>
         */
        suspend inline fun <reified T : Any> executeRequest(request: suspend () -> HttpStatement): ApiResponse<T> =
            try {
                val response = request.invoke().execute()
                if (response.status == HttpStatusCode.Created) {
                    val result: T = response.body()
                    ApiResponse.Created(result)
                } else if (response.status != HttpStatusCode.NoContent) {
                    val result: T = response.body()
                    ApiResponse.Success(result)
                } else {
                    ApiResponse.NoContent
                }
            } catch (exception: Exception) {
                ApiResponse.Error(exception.message.orEmpty())
            }
    }

const val RESPONSE_TIMEOUT_DEFAULT = 60_000L
