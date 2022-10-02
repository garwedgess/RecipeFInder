package eu.wedgess.recipefinder.data.repository

import eu.wedgess.recipefinder.data.datasource.ingredients.IngredientsCacheDataSource
import eu.wedgess.recipefinder.data.datasource.ingredients.IngredientsRemoteDataSource
import eu.wedgess.recipefinder.data.datasource.recipes.RecipesCacheDataSource
import eu.wedgess.recipefinder.data.model.IngredientsApiResponseData
import eu.wedgess.recipefinder.domain.RecipeIngredientsRepository
import eu.wedgess.recipefinder.domain.entities.IngredientsWithRecipesEntity
import eu.wedgess.recipefinder.domain.entities.RecipeEntity
import eu.wedgess.recipefinder.domain.entities.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Recipe ingredients repository implementation - ideally this would be split to tow
 * repositories. One for Ingredients and one for Recipes.
 *
 * @property ingredientsRemoteDataSource - the remote data source to fetch ingredients from the API
 * @property ingredientsCacheDataSource - the cache data source to fetch ingredients and remove them from memory
 * @property recipesCacheDataSource - the cache data source for the recipes and to get recipes compatible with ingredients
 *
 */
@Singleton
class RecipeIngredientsRepositoryImpl @Inject constructor(
    private val ingredientsRemoteDataSource: IngredientsRemoteDataSource,
    private val ingredientsCacheDataSource: IngredientsCacheDataSource,
    private val recipesCacheDataSource: RecipesCacheDataSource
) : RecipeIngredientsRepository {

    /**
     * Get available ingredients from the [IngredientsRemoteDataSource] if refresh is true
     * if refresh ius false then get from [IngredientsCacheDataSource]. If cache is empty
     * then fetch from the network instead. Once fetched from the network save the ingredients to cache.
     *
     * @return - [IngredientsApiResponseData] the caller must handle the different [Resource] types
     */
    override suspend fun getAvailableIngredients(isRefresh: Boolean): Resource<IngredientsWithRecipesEntity> {
        return withContext(Dispatchers.IO) {
            if (isRefresh) {
                fetchNetworkIngredientsAndCacheResult()
            } else {
                val cachedIngredientsResponse = ingredientsCacheDataSource.getAvailableIngredients()
                if (cachedIngredientsResponse is Resource.Empty) {
                    fetchNetworkIngredientsAndCacheResult()
                } else {
                    handleCacheIngredientsResponse(cachedIngredientsResponse)
                }

            }
        }
    }

    /**
     * Remove an ingredient from the cached ingredients
     *
     * @param ingredient - the ingredient to remove
     * @return
     */
    override suspend fun removeIngredient(ingredient: String): Resource<IngredientsWithRecipesEntity> {
        return withContext(Dispatchers.IO) {
            val ingredients = ingredientsCacheDataSource.removeIngredient(ingredient)
            if (ingredients.isEmpty()) {
                Resource.Empty
            } else {
                val compatibleRecipes = recipesCacheDataSource.getCompatibleRecipes(ingredients)
                createIngredientsWithRecipesSuccessResponse(
                    ingredients = ingredients,
                    recipes = compatibleRecipes
                )
            }
        }
    }


    /**
     * Fetch network ingredients and cache result
     *
     * @return hte [Resource]
     */
    private suspend fun fetchNetworkIngredientsAndCacheResult(): Resource<IngredientsWithRecipesEntity> {
        return when (val networkResponse = ingredientsRemoteDataSource.getAvailableIngredients()) {
            is Resource.Success -> {
                val networkIngredients = networkResponse.data
                ingredientsCacheDataSource.cacheAllAvailableIngredients(networkIngredients)
                val allRecipes =
                    recipesCacheDataSource.getCompatibleRecipes(networkIngredients)
                createIngredientsWithRecipesSuccessResponse(
                    networkIngredients,
                    allRecipes
                )
            }
            is Resource.Empty -> {
                Resource.Empty
            }
            is Resource.Error -> {
                networkResponse
            }
        }
    }

    /**
     * Handle the cache ingredients response
     *
     * @param cachedIngredientsResponse - the caches ingredients response
     * @return - the [Resource]
     */
    private suspend fun handleCacheIngredientsResponse(cachedIngredientsResponse: Resource<List<String>>): Resource<IngredientsWithRecipesEntity> {
        return when (cachedIngredientsResponse) {
            is Resource.Success -> {
                val compatibleRecipes =
                    recipesCacheDataSource.getCompatibleRecipes(
                        cachedIngredientsResponse.data
                    )
                createIngredientsWithRecipesSuccessResponse(
                    ingredients = cachedIngredientsResponse.data,
                    compatibleRecipes
                )
            }
            is Resource.Empty -> Resource.Empty
            is Resource.Error -> cachedIngredientsResponse
        }
    }


    /**
     * Create ingredients with recipes success response
     *
     * @param ingredients - available ingredients
     * @param recipes - compatible recipes
     * @return
     */
    private fun createIngredientsWithRecipesSuccessResponse(
        ingredients: List<String>,
        recipes: List<RecipeEntity>
    ): Resource<IngredientsWithRecipesEntity> {
        return Resource.Success(
            IngredientsWithRecipesEntity(
                availableIngredients = ingredients,
                recipes = recipes
            )
        )
    }
}