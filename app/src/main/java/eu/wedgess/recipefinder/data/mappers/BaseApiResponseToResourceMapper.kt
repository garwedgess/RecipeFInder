package eu.wedgess.recipefinder.data.mappers

import eu.wedgess.recipefinder.data.model.ApiResponse
import eu.wedgess.recipefinder.data.model.IngredientsApiResponseData
import eu.wedgess.recipefinder.domain.Mapper
import eu.wedgess.recipefinder.domain.entities.Resource

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