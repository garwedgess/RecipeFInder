package eu.wedgess.recipefinder.data.model

import kotlinx.serialization.SerializationException
import java.io.IOException


/**
 * This class wraps the response data returned from the API which will be later handled in a ViewModel
 *
 * @param T - Type of the response class
 * @param E - Type of the error class
 */
sealed class ApiResponse<out T, out E> {
    /**
     * Represents successful network responses (2xx).
     */
    data class Success<T>(val body: T) : ApiResponse<T, Nothing>()

    sealed class Error<E> : ApiResponse<Nothing, E>() {
        /**
         * Represents server (50x) and client (40x) errors.
         */
        data class HttpError<E>(val code: Int, val errorBody: E?) : Error<E>()

        /**
         * Represent IOExceptions and connectivity issues.
         */
        data class NetworkError(val exception: IOException) : Error<Nothing>()

        /**
         * Represent SerializationExceptions.
         */
        data class SerializationError(val exception: SerializationException) : Error<Nothing>()
    }
}