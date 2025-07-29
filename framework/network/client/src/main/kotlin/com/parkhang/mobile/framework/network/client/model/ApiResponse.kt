package com.parkhang.mobile.framework.network.client.model

import com.parkhang.mobile.framework.network.client.model.ApiSuccessType.Created
import com.parkhang.mobile.framework.network.client.model.ApiSuccessType.NoContent
import com.parkhang.mobile.framework.network.client.model.ApiSuccessType.Success

/**
 * [ApiResponse]
 */
sealed class ApiResponse<out T> {
    data class Created<T>(
        val value: T,
    ) : ApiResponse<T>()

    data class Success<T>(
        val value: T,
    ) : ApiResponse<T>()

    data object NoContent : ApiResponse<Nothing>()

    data class Error(
        val error: String?,
    ) : ApiResponse<Nothing>()

    data class Unauthorized(
        val error: String,
    ) : ApiResponse<Nothing>()
}

/**
 * Support function to execute a block that returns [ApiResponse<T>] and convert to Result<T>
 *
 * http 204 - No Content will be treated as an error. If expected, use [withNullableResult] or [withoutResult] instead
 *
 * @return Result<T>
 */
inline fun <reified T : Any> withResult(block: () -> ApiResponse<T>): Result<T> {
    val response: ApiResponse<T> = block()
    return response.toResultWrappedInSuccessType().map { successType ->
        when (successType) {
            is Success -> successType.value
            is Created -> successType.value
            is NoContent -> return Result.failure(Exception("No Content"))
        }
    }
}

/**
 * Convert ApiResponse<T> to Kotlin Result encapsulation using custom exceptions
 * The network exceptions will be logged by the Network layer
 *
 * If the response is a success, the actual value is wrapped in [ApiSuccessType]
 *
 *
 * @return Result<ApiResponseSuccess<T>>
 */
fun <T> ApiResponse<T>.toResultWrappedInSuccessType(): Result<ApiSuccessType<T>> = when (this) {
    is ApiResponse.Success -> Result.success(ApiSuccessType.Success(value))
    is ApiResponse.NoContent -> Result.failure(Exception("No Content"))
    is ApiResponse.Unauthorized -> Result.failure(Exception("Unauthorized: $error"))
    is ApiResponse.Error -> Result.failure(Exception("Error: $error"))
    else -> Result.failure(Exception("Unhandled ApiResponse type: $this"))
}

/**
 * \`ApiSuccessType\` is a sealed class used to wrap the API response body for successful responses.
 *
 * @param T The type of the response body.
 */
sealed class ApiSuccessType<out T> {
    data class Success<T>(
        val value: T,
    ) : ApiSuccessType<T>() // http 200

    data class Created<T>(
        val value: T,
    ) : ApiSuccessType<T>() // http 201

    data object NoContent : ApiSuccessType<Nothing>() // http 204
}
