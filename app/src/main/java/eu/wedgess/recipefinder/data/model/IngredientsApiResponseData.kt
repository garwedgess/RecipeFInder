package eu.wedgess.recipefinder.data.model

/**
 * A typealias for the [ApiResponse].
 * Sets the error type of the ApiResponse to always be a nullable [String]
 *
 * @param D - Type of the response class
 */
typealias IngredientsApiResponseData<D> = ApiResponse<D, String?>