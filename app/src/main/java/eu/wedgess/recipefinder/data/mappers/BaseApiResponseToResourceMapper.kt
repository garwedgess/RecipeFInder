package eu.wedgess.recipefinder.data.mappers

import eu.wedgess.recipefinder.data.model.ApiResponse
import eu.wedgess.recipefinder.data.model.IngredientsApiResponseData
import eu.wedgess.recipefinder.domain.Mapper
import eu.wedgess.recipefinder.domain.entities.Resource

/**
 * Base abstract class to handle mapping of [ApiResponse] to [Resource]. All errors are converted here,
 * the child classes must implement [handleSuccessResponse] to determine if the result is a
 * [Resource.Success] or
 * [Resource.Empty]
 *
 * @param E - entity class
 * @param D - data class
 */
abstract class BaseApiResponseToResourceMapper<E, D> :
    Mapper<Resource<E>, IngredientsApiResponseData<D>> {

    abstract fun handleSuccessResponse(data: ApiResponse.Success<D>): Resource<E>

    override fun mapFromEntity(entity: Resource<E>): IngredientsApiResponseData<D> {
        throw UnsupportedOperationException("Cannot map Resource<E> to IngredientsApiResponseData<D>")
    }

    override fun mapToEntity(data: IngredientsApiResponseData<D>): Resource<E> {
        return when (data) {
            is ApiResponse.Success -> {
                handleSuccessResponse(data)
            }
            is ApiResponse.Error -> {
                when (data) {
                    is ApiResponse.Error.HttpError -> {
                        Resource.Error(data.errorBody)
                    }
                    is ApiResponse.Error.NetworkError -> {
                        Resource.Error(data.exception.message)
                    }
                    is ApiResponse.Error.SerializationError -> {
                        Resource.Error(data.exception.message)

                    }
                }
            }
        }
    }

}