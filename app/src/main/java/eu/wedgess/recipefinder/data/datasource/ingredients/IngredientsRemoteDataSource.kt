package eu.wedgess.recipefinder.data.datasource.ingredients

import eu.wedgess.recipefinder.data.api.IngredientsApi
import eu.wedgess.recipefinder.data.mappers.AvailableIngredientResponseMapper
import eu.wedgess.recipefinder.domain.entities.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngredientsRemoteDataSource @Inject constructor(
    private val api: IngredientsApi,
    private val availableIngredientsApiResponseDataMapper: AvailableIngredientResponseMapper
) :
    IngredientsDataSource {

    /**
     * Get available ingredients from the remote API
     *
     * @return
     */
    override suspend fun getAvailableIngredients(): Resource<List<String>> {
        val response = api.getAvailableIngredients()
        return availableIngredientsApiResponseDataMapper.mapToEntity(response)
    }
}