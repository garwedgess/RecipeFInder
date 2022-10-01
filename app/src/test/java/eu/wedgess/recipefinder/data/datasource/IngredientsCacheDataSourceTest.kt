package eu.wedgess.recipefinder.data.datasource

import eu.wedgess.recipefinder.data.datasource.ingredients.IngredientsCacheDataSource
import eu.wedgess.recipefinder.data.model.ApiResponse
import eu.wedgess.recipefinder.domain.entities.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


/**
 * This class test the correct [Resource] and data is returned from cache
 */
@OptIn(ExperimentalCoroutinesApi::class)
class IngredientsCacheDataSourceTest {

    private lateinit var ingredientsCacheDataSource: IngredientsCacheDataSource

    @Before
    fun setup() {
        ingredientsCacheDataSource = IngredientsCacheDataSource()
    }

    /**
     * Given - No ingredients are set
     * When - [IngredientsCacheDataSource.getAvailableIngredients] is called
     * Then - [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Empty]
     */
    @Test
    fun repositoryGetIngredientsReturnsEmptyWhenThereIsNoData() {
        runTest {
            ingredientsCacheDataSource.cacheAllAvailableIngredients(emptyList())
            val response = ingredientsCacheDataSource.getAvailableIngredients()
            assertIs<Resource.Empty>(response)
        }
    }

    /**
     * Given - Ingredients are set
     * When - [IngredientsCacheDataSource.getAvailableIngredients] is called
     * Then - [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Success]
     */
    @Test
    fun repositoryGetIngredientsReturnsSuccessResponse() {
        runTest {
            ingredientsCacheDataSource.cacheAllAvailableIngredients(listOf("Meat", "onion", "cheese"))
            val response = ingredientsCacheDataSource.getAvailableIngredients()
            assertIs<Resource.Success<List<String>>>(response)
        }
    }

    /**
     * Given - Ingredients are set
     * When - [IngredientsCacheDataSource.getAvailableIngredients] is called
     * Then - [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Success] with correct data
     */
    @Test
    fun repositoryGetIngredientsReturnsExpectedData() {
        val expectedData = listOf("Meat", "onion", "cheese")
        runTest {
            ingredientsCacheDataSource.cacheAllAvailableIngredients(expectedData)
            val response = ingredientsCacheDataSource.getAvailableIngredients()
            assertEquals(expectedData, (response as Resource.Success).data)
        }
    }


    /**
     * Given - Ingredients are set
     * When - [IngredientsCacheDataSource.removeIngredient] is called
     * Then - [IngredientsCacheDataSource.removeIngredient] returns data with ingredient removed
     */
    @Test
    fun repositoryRemoveIngredientsReturnsCorrectData() {
        val responseData = listOf("Meat", "onion", "cheese")
        runTest {
            ingredientsCacheDataSource.cacheAllAvailableIngredients(responseData)
            val response = ingredientsCacheDataSource.removeIngredient(responseData.first())
            assertEquals(responseData.subList(1, responseData.size), response)
        }
    }

}