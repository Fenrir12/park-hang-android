package com.parkhang.mobile.framework.network.client.model

import com.parkhang.mobile.framework.network.client.config.addAnonymousHeader
import com.parkhang.mobile.framework.network.client.config.addBaseUrl
import com.parkhang.mobile.framework.network.client.config.addBearerToken
import com.parkhang.mobile.framework.network.client.config.addRequestParams
import kotlin.text.isEmpty
import kotlin.text.isNotEmpty

/**
 * Represents the type of network request.
 *
 * There are two variants:
 * - [Anonymous]: For requests that do not require user authentication.
 * - [Authenticated]: For requests that require a valid access token.
 */
sealed class NetworkRequestType {
    /** Represents a network request made without user authentication. */
    data object Anonymous : NetworkRequestType()

    /** Represents a network request made with user authentication. */
    data object Authenticated : NetworkRequestType()
}

/**
 * A factory for adapting network request parameters based on different authentication states.
 *
 * This class creates lambda functions (configuration blocks) that you can apply to a
 * [NetworkRequestBuilder] in order to build a network request. It supports:
 * - Anonymous requests via [anonymous]
 * - Authenticated requests via [authenticated]
 *
 * ``` Usage example
 * // For an authenticated request:
 * val authenticatedConfig = factory.authenticated()
 * val authenticatedRequest = NetworkRequestBuilder().apply(authenticatedConfig)
 *
 * // For an anonymous request:
 * val anonymousConfig = factory.anonymous()
 * val anonymousRequest = NetworkRequestBuilder().apply(anonymousConfig)
 *
 * // For a request that adapts based on token availability:
 * val adaptiveConfig = factory.anonymousOrAuthenticated()
 * val adaptiveRequest = NetworkRequestBuilder().apply(adaptiveConfig)
 * ```
 *
 * @property loadBaseUrl Suspend function returning the base URL for the network request.
 * @property loadAccessToken Suspend function returning the access token; must be non-empty for authenticated requests.
 */
class NetworkAdaptRequestFactory(
    val loadBaseUrl: suspend () -> String,
    val loadAccessToken: suspend () -> String,
) {
    /**
     * Generates a network request configuration for anonymous requests.
     *
     * This function loads the necessary parameters and returns a lambda that configures a
     * [NetworkRequestBuilder] by:
     * - Adding the base URL.
     * - Adding an anonymous header.
     *
     * ### Usage Example
     * ```kotlin
     * val config = factory.anonymous()
     * val requestBuilder = NetworkRequestBuilder().apply(config)
     * ```
     *
     * @return A lambda function that configures a [NetworkRequestBuilder] for anonymous requests.
     */
    suspend fun anonymous(): NetworkRequestBuilder.() -> Unit {
        val baseUrl = loadBaseUrl()

        return addRequestParams {
            addBaseUrl(baseUrl)
            addAnonymousHeader()
        }
    }

    suspend fun authenticated(): NetworkRequestBuilder.() -> Unit {
        val baseUrl = loadBaseUrl()
        val token = loadAccessToken()

        if (token.isEmpty()) {
            throw OperationNotPermittedException(
                cause = Throwable("Access token is empty (or user is not logged) in a required authenticated request"),
            )
        }

        return addRequestParams {
            addBaseUrl(baseUrl)
            addBearerToken(token)
        }
    }

    suspend fun anonymousOrAuthenticated(): NetworkRequestBuilder.() -> Unit {
        val baseUrl = loadBaseUrl()
        val token = loadAccessToken()

        return addRequestParams {
            addBaseUrl(baseUrl)

            if (token.isNotEmpty()) {
                addBearerToken(token)
            } else {
                addAnonymousHeader()
            }
        }
    }
}
