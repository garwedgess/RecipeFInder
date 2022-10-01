package eu.wedgess.recipefinder.data.mappers

import eu.wedgess.recipefinder.data.model.ApiResponse
import eu.wedgess.recipefinder.data.model.AvailableIngredientsData
import eu.wedgess.recipefinder.domain.entities.Resource

class AvailableIngredientResponseMapper :
    BaseApiResponseToResourceMapper<List<String>, AvailableIngredientsData>() {

    override fun handleSuccessResponse(data: ApiResponse.Success<AvailableIngredientsData>): Resource<List<String>> {
        val ingredients = data.body.ingredients
        return if (ingredients.isEmpty()) {
            Resource.Empty
        } else {
            Resource.Success(ingredients)
        }
    }
}