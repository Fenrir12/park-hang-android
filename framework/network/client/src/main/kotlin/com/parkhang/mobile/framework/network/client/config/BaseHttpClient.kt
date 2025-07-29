package com.parkhang.mobile.framework.network.client.config

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.internal.toImmutableList

class BaseHttpClient(
    private val baseHttpEngine: BaseHttpEngine,
) {
    fun build(): HttpClient {
        val engine = baseHttpEngine.build()

        return HttpClient(OkHttp) {
            engine {
                preconfigured = engine
            }

            baseConfig().invoke(this)
        }
    }
}

fun baseConfig(): HttpClientConfig<*>.() -> Unit = {
    expectSuccess = true

    install(ContentNegotiation) {
        json(
            Json {
                isLenient = true
                ignoreUnknownKeys = true
                coerceInputValues = true
            },
        )
    }

    install(HttpRequestRetry) {
        maxRetries = RETRY_STRATEGY_MAXIMUM_COUNT

        retryIf { request, response ->
            response.status.isRetryable() && request.method.isGet()
        }

        // By default, Ktor does not retry on exceptions
        // - IOException can happen on DNS resolution/lookup failure, etc.
        // - SocketTimeoutException can happen if the network is temporarily
        //   unavailable, bad wifi, etc.
        retryOnExceptionIf { _, cause ->
            cause is java.net.SocketTimeoutException || cause is java.io.IOException
        }

        exponentialDelay(
            base = RETRY_STRATEGY_EXPONENTIAL_BASE_DELAY,
        )
    }

    install(Resources)
    install(HttpTimeout)
    install(ContentEncoding) {
        gzip()
        deflate()
    }
}

/***
 * Checks if the status code is a 5XX that can replayed
 *
 * Note: we ignore 4XX errors as they require a user actions
 *       to fix (validation errors).
 *
 * @return true if the status code is a retryable 5XX, false otherwise.
 */
fun HttpStatusCode.isRetryable(): Boolean = this.value in RETRYABLE_CLIENT_STATUS_CODES || this.value in RETRYABLE_SERVER_STATUS_CODES

/***
 * Checks if the method is a GET.
 *
 * Some POST requests can be replayed, but most of them
 * cause changes to the server state (eg: db insert/update/deletion).
 *
 * We play safe by retrying only GET requests.
 *
 * @return true if the method is a GET, false otherwise.
 */
fun HttpMethod.isGet(): Boolean = this == HttpMethod.Get

const val HTTP_TIMEOUT_DEFAULT_MILLIS = 10000L

const val RETRY_STRATEGY_MAXIMUM_COUNT = 2
const val RETRY_STRATEGY_EXPONENTIAL_BASE_DELAY = 2.0

val RETRYABLE_CLIENT_STATUS_CODES = listOf(HttpStatusCode.RequestTimeout.value).toImmutableList()
val RETRYABLE_SERVER_STATUS_CODES = HttpStatusCode.InternalServerError.value..599
