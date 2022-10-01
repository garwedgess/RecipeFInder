package eu.wedgess.recipefinder.data.utils

import eu.wedgess.recipefinder.data.model.ApiResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.utils.io.errors.*
import kotlinx.serialization.SerializationException


/**
 * An extension function to wrap make a HTTP request and wrap the response as an [ApiResponse]
 *
 * @param T - Type of the response class
 * @param E - Type of the error class
 * @param block - HttpRequestBuilder to execute
 * @receiver - the Ktor [HttpClient]
 * @return - wrapped response as an [ApiResponse]
 */
suspend inline fun <reified T, reified E> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): ApiResponse<T, E> =
    try {
        val response = request { block() }
        ApiResponse.Success(response.body())
    } catch (e: ClientRequestException) {
        ApiResponse.Error.HttpError(e.response.status.value, e.errorBody())
    } catch (e: ServerResponseException) {
        ApiResponse.Error.HttpError(e.response.status.value, e.errorBody())
    } catch (e: IOException) {
        ApiResponse.Error.NetworkError(e)
    } catch (e: SerializationException) {
        ApiResponse.Error.SerializationError(e)
    }


/**
 * Extension function for getting the error response body from the [ResponseException]
 *
 * @param E - Type of the error class
 * @return - nullable error class
 */
suspend inline fun <reified E> ResponseException.errorBody(): E? =
    try {
        response.body()
    } catch (e: SerializationException) {
        null
    }