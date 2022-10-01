package eu.wedgess.recipefinder.domain.entities

// For wrapping our results before sending the data to the UI
sealed class Resource<out T> {

    data class Success<T>(val data: T): Resource<T>()

    data class Error(val message: String?): Resource<Nothing>()

    object Empty: Resource<Nothing>()
}
