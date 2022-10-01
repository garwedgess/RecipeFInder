package eu.wedgess.recipefinder.data.datasource

import eu.wedgess.recipefinder.helpers.TestHelper
import eu.wedgess.recipefinder.data.datasource.recipes.RecipesCacheDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


/**
 * This class test the correct [List<RecipeData>] is returned from cache
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RecipesCacheDataSourceTest {

    private lateinit var recipesCacheDataSource: RecipesCacheDataSource

    @Before
    fun setup() {
        recipesCacheDataSource = RecipesCacheDataSource()
    }

    /**
     * Given - Recipes list
     * When - [RecipesCacheDataSource.getAllRecipes] is called
     * Then - [RecipesCacheDataSource.getAllRecipes] returns [List<RecipeData>]
     */
    @Test
    fun cacheDataSourceGetAllRecipesReturnsExpectedRecipeData() {
        val recipesDataList = TestHelper.createAllRecipesList()
        runTest {
            val response = recipesCacheDataSource.getAllRecipes()
            assertEquals(recipesDataList, response)
        }
    }

    /**
     * Given - Ingredients list
     * When - [RecipesCacheDataSource.getCompatibleRecipes] is called
     * Then - [RecipesCacheDataSource.getCompatibleRecipes] returns correct [RecipeData]
     */
    @Test
    fun cacheDataSourceGetCompatibleRecipesReturnsMeatballs() {
        runTest {
            val response =
                recipesCacheDataSource.getCompatibleRecipes(listOf("Meat", "egg", "onion"))
            assertEquals(TestHelper.getRecipeByName("Meatloaf"), response.first())
        }
    }


    /**
     * Given - Ingredients list
     * When - [RecipesCacheDataSource.getCompatibleRecipes] is called
     * Then - [RecipesCacheDataSource.getCompatibleRecipes] returns empty list
     */
    @Test
    fun cacheDataSourceGetCompatibleRecipesReturnsEmptyList() {
        runTest {
            val response = recipesCacheDataSource.getCompatibleRecipes(listOf("onion"))
            assertEquals(emptyList(), response)
        }
    }

}