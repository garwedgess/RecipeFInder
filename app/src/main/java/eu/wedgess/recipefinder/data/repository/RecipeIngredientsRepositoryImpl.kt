package eu.wedgess.recipefinder.data.repository

import eu.wedgess.recipefinder.data.datasource.ingredients.IngredientsCacheDataSource
import eu.wedgess.recipefinder.data.datasource.ingredients.IngredientsRemoteDataSource
import eu.wedgess.recipefinder.data.datasource.recipes.RecipesCacheDataSource
import eu.wedgess.recipefinder.data.model.IngredientsApiResponseData
import eu.wedgess.recipefinder.data.model.RecipeData
import eu.wedgess.recipefinder.domain.RecipeIngredientsRepository
import eu.wedgess.recipefinder.domain.entities.IngredientsWithRecipesEntity
import eu.wedgess.recipefinder.domain.entities.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeIngredientsRepositoryImpl @Inject constructor(
    private val ingredientsRemoteDataSource: IngredientsRemoteDataSource,
    private val ingredientsCacheDataSource: IngredientsCacheDataSource,
    private val recipesCacheDataSource: RecipesCacheDataSource
) : RecipeIngredientsRepository {

    /**
     * Get available ingredients from the [IngredientsApi]
     *
     * @return - [IngredientsApiResponseData] the caller must handle the different [eu.wedgess.recipefinder.data.model.ApiResponse] types
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
                    handleCachesIngredientsResponse(cachedIngredientsResponse)
                }

            }
        }
    }

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

    private suspend fun handleCachesIngredientsResponse(cachedIngredientsResponse: Resource<List<String>>): Resource<IngredientsWithRecipesEntity> {
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


    private fun createIngredientsWithRecipesSuccessResponse(
        ingredients: List<String>,
        recipes: List<RecipeData>
    ): Resource<IngredientsWithRecipesEntity> {
        return Resource.Success(
            IngredientsWithRecipesEntity(
                availableIngredients = ingredients,
                recipes = recipes
            )
        )
    }
}