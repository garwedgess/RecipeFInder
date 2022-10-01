package eu.wedgess.recipefinder.data.datasource.ingredients

import eu.wedgess.recipefinder.domain.entities.Resource

/**
 * Ingredients data source - implemented by [IngredientsRemoteDataSource] and [IngredientsCacheDataSource]
 *
 * @constructor Create empty Ingredients data source
 */
interface IngredientsDataSource {

    suspend fun getAvailableIngredients(): Resource<List<String>>

}