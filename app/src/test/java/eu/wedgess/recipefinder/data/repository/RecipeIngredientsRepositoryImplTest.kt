package eu.wedgess.recipefinder.data.repository

import eu.wedgess.recipefinder.data.datasource.ingredients.IngredientsCacheDataSource
import eu.wedgess.recipefinder.data.datasource.ingredients.IngredientsRemoteDataSource
import eu.wedgess.recipefinder.data.datasource.recipes.RecipesCacheDataSource
import eu.wedgess.recipefinder.data.model.ApiResponse
import eu.wedgess.recipefinder.domain.entities.IngredientsWithRecipesEntity
import eu.wedgess.recipefinder.domain.entities.Resource
import eu.wedgess.recipefinder.helpers.TestHelper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertIs


/**
 * This class test the correct [Resource] is returned from the datasources and
 * which methods are called and the order in which they are called. For example - when cache or network is called.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RecipeIngredientsRepositoryImplTest {

    private lateinit var recipeIngredientsRepositoryImpl: RecipeIngredientsRepositoryImpl

    @MockK
    private lateinit var ingredientsRemoteDataSource: IngredientsRemoteDataSource

    @MockK
    private lateinit var ingredientsCacheDataSource: IngredientsCacheDataSource

    @MockK
    private lateinit var recipesCacheDataSource: RecipesCacheDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        recipeIngredientsRepositoryImpl = RecipeIngredientsRepositoryImpl(
            ingredientsRemoteDataSource,
            ingredientsCacheDataSource,
            recipesCacheDataSource
        )
    }

    @After
    fun teardown() = unmockkAll()


    // region Test execution of network & cache along with order of function execution

    /**
     * Given - [IngredientsRemoteDataSource.getAvailableIngredients] returns [Resource.Success] with data and
     * [RecipesCacheDataSource.getCompatibleRecipes] returns [List<RecipeEntity>]
     * When - [RecipeIngredientsRepositoryImpl.getAvailableIngredients] is called with isRefresh=true
     * Then - [IngredientsRemoteDataSource.getAvailableIngredients], [IngredientsCacheDataSource.cacheAllAvailableIngredients] &
     * [RecipesCacheDataSource.getCompatibleRecipes] is called exactly once
     */
    @Test
    fun repositoryGetAvailableIngredientsExecutedWithRefreshTrueCallsRemoteGetIngredientsAndCachesIngredientsExactlyOnce() {
        val availableIngredients = listOf("Meat", "parmesan", "onion")
        coEvery { ingredientsRemoteDataSource.getAvailableIngredients() } returns Resource.Success(
            availableIngredients
        )
        coEvery { recipesCacheDataSource.getCompatibleRecipes(any()) } returns listOf(
            TestHelper.getRecipeEntityByName("Meatball")
        )
        runTest {
            recipeIngredientsRepositoryImpl.getAvailableIngredients(isRefresh = true)
        }
        coVerify(exactly = 1) {
            ingredientsRemoteDataSource.getAvailableIngredients()
            ingredientsCacheDataSource.cacheAllAvailableIngredients(availableIngredients)
            recipesCacheDataSource.getCompatibleRecipes(availableIngredients)
        }
    }

    /**
     * Given - [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Empty] and
     * [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Success]
     * When - [RecipeIngredientsRepositoryImpl.getAvailableIngredients] is called with isRefresh=false
     * but [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Empty]
     * Then - [IngredientsRemoteDataSource.getAvailableIngredients] and [IngredientsCacheDataSource.cacheAllAvailableIngredients] is called in order
     */
    @Test
    fun repositoryGetAvailableIngredientsExecutedWithRefreshFalseButCacheIsEmptyCallsRemoteGetIngredientsAndCachesIngredientsInOrder() {
        val availableIngredients = listOf("Meat", "parmesan", "onion")
        coEvery { ingredientsRemoteDataSource.getAvailableIngredients() } returns Resource.Success(
            availableIngredients
        )
        coEvery { ingredientsCacheDataSource.getAvailableIngredients() } answers { Resource.Empty }
        coEvery { recipesCacheDataSource.getCompatibleRecipes(any()) } returns listOf(
            TestHelper.getRecipeEntityByName("Meatball")
        )
        runTest {
            recipeIngredientsRepositoryImpl.getAvailableIngredients(isRefresh = false)
        }
        coVerifyOrder {
            ingredientsRemoteDataSource.getAvailableIngredients()
            ingredientsCacheDataSource.cacheAllAvailableIngredients(availableIngredients)
            recipesCacheDataSource.getCompatibleRecipes(availableIngredients)
        }
    }

    /**
     * Given - [IngredientsRemoteDataSource.getAvailableIngredients] returns [Resource.Success] with data and
     * [RecipesCacheDataSource.getCompatibleRecipes] returns [List<RecipeEntity>]
     * When - [RecipeIngredientsRepositoryImpl.getAvailableIngredients] is called with isRefresh=true
     * Then - [IngredientsCacheDataSource.cacheAllAvailableIngredients] is not called
     */
    @Test
    fun repositoryGetAvailableIngredientsExecutedWithRefreshTrueThenCacheGetIngredientsIsNotExecuted() {
        val availableIngredients = listOf("Meat", "parmesan", "onion")
        coEvery { ingredientsRemoteDataSource.getAvailableIngredients() } returns Resource.Success(
            availableIngredients
        )
        coEvery { recipesCacheDataSource.getCompatibleRecipes(any()) } returns listOf(
            TestHelper.getRecipeEntityByName("Meatball")
        )
        runTest {
            recipeIngredientsRepositoryImpl.getAvailableIngredients(isRefresh = true)
        }
        coVerify(exactly = 0) {
            ingredientsCacheDataSource.getAvailableIngredients()
        }
    }


    /**
     * Given - [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Empty] and
     * [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Success]
     * When - [RecipeIngredientsRepositoryImpl.getAvailableIngredients] is called with isRefresh=false but
     * [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Empty]
     * Then - [IngredientsRemoteDataSource.getAvailableIngredients], [RecipesCacheDataSource.getCompatibleRecipes] &
     * [IngredientsCacheDataSource.cacheAllAvailableIngredients] is called exactly once
     */
    @Test
    fun repositoryGetAvailableIngredientsExecutedWithRefreshFalseButCacheIsEmptyCallsRemoteGetIngredientsAndCachesIngredientsExactlyOnce() {
        val availableIngredients = listOf("Meat", "parmesan", "onion")
        coEvery { ingredientsRemoteDataSource.getAvailableIngredients() } returns Resource.Success(
            availableIngredients
        )
        coEvery { ingredientsCacheDataSource.getAvailableIngredients() } answers { Resource.Empty }
        coEvery { recipesCacheDataSource.getCompatibleRecipes(any()) } returns listOf(
            TestHelper.getRecipeEntityByName("Meatball")
        )
        runTest {
            recipeIngredientsRepositoryImpl.getAvailableIngredients(isRefresh = false)
        }
        coVerify(exactly = 1) {
            ingredientsRemoteDataSource.getAvailableIngredients()
            ingredientsCacheDataSource.cacheAllAvailableIngredients(availableIngredients)
            recipesCacheDataSource.getCompatibleRecipes(availableIngredients)
        }
    }

    /**
     * Given - [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Empty] and
     * [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Success]
     * When - [RecipeIngredientsRepositoryImpl.getAvailableIngredients] is called with
     * isRefresh=false but [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Empty]
     * Then - [IngredientsCacheDataSource.getAvailableIngredients] & [IngredientsRemoteDataSource.getAvailableIngredients]
     * [IngredientsCacheDataSource.cacheAllAvailableIngredients] & [RecipesCacheDataSource.getCompatibleRecipes]
     * is executed once
     */
    @Test
    fun repositoryGetAvailableIngredientsExecutedWithRefreshFalseButCacheIsEmptyThenCachesAndRemoteIngredientsIsExecutedOnce() {
        val availableIngredients = listOf("Meat", "parmesan", "onion")
        coEvery { ingredientsRemoteDataSource.getAvailableIngredients() } returns Resource.Success(
            availableIngredients
        )
        coEvery { ingredientsCacheDataSource.getAvailableIngredients() } answers { Resource.Empty }
        coEvery { recipesCacheDataSource.getCompatibleRecipes(any()) } returns listOf(
            TestHelper.getRecipeEntityByName("Meatball")
        )
        runTest {
            recipeIngredientsRepositoryImpl.getAvailableIngredients(isRefresh = false)
        }
        coVerify(exactly = 1) {
            ingredientsCacheDataSource.getAvailableIngredients()
            ingredientsRemoteDataSource.getAvailableIngredients()
            ingredientsCacheDataSource.cacheAllAvailableIngredients(availableIngredients)
            recipesCacheDataSource.getCompatibleRecipes(availableIngredients)
        }
    }

    /**
     * Given - [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Success] &
     * [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Empty]
     * When - [RecipeIngredientsRepositoryImpl.getAvailableIngredients] is called with
     * isRefresh=false but [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Empty]
     * Then - [IngredientsCacheDataSource.getAvailableIngredients], [IngredientsRemoteDataSource.getAvailableIngredients]
     * & [IngredientsCacheDataSource.cacheAllAvailableIngredients] & [RecipesCacheDataSource.getCompatibleRecipes] is executed in order once
     */
    @Test
    fun repositoryGetAvailableIngredientsExecutedWithRefreshFalseButCacheIsEmptyThenCachesAndRemoteIngredientsIsExecutedInOrder() {
        val availableIngredients = listOf("Meat", "parmesan", "onion")
        coEvery { ingredientsRemoteDataSource.getAvailableIngredients() } returns Resource.Success(
            availableIngredients
        )
        coEvery { ingredientsCacheDataSource.getAvailableIngredients() } answers { Resource.Empty }
        coEvery { recipesCacheDataSource.getCompatibleRecipes(any()) } returns listOf(
            TestHelper.getRecipeEntityByName("Meatball")
        )
        runTest {
            recipeIngredientsRepositoryImpl.getAvailableIngredients(isRefresh = false)
        }
        coVerifyOrder {
            ingredientsCacheDataSource.getAvailableIngredients()
            ingredientsRemoteDataSource.getAvailableIngredients()
            ingredientsCacheDataSource.cacheAllAvailableIngredients(availableIngredients)
            recipesCacheDataSource.getCompatibleRecipes(availableIngredients)
        }
    }

    //endregion

    //region Network Data Source testing

    /**
     * Given - [IngredientsRemoteDataSource.getAvailableIngredients] returns [Resource.Success] with data and
     * [RecipesCacheDataSource.getCompatibleRecipes] returns [RecipeData]
     * When - [RecipeIngredientsRepositoryImpl.getAvailableIngredients] is called
     * Then - [ApiResponse.Success] is returned
     */
    @Test
    fun repositoryGetIngredientsReturnsSuccessResponse() {
        val availableIngredients = listOf("Meat", "parmesan", "onion")
        coEvery { ingredientsRemoteDataSource.getAvailableIngredients() } returns Resource.Success(
            availableIngredients
        )
        coEvery { recipesCacheDataSource.getCompatibleRecipes(any()) } returns listOf(
            TestHelper.getRecipeEntityByName("Meatball")
        )
        runTest {
            val response = recipeIngredientsRepositoryImpl.getAvailableIngredients(isRefresh = true)
            assertIs<Resource.Success<List<String>>>(response)
        }
    }

    /**
     * Given - [IngredientsRemoteDataSource.getAvailableIngredients] returns [Resource.Success] with data and
     * [RecipesCacheDataSource.getCompatibleRecipes] returns [RecipeData]
     * When - [RecipeIngredientsRepositoryImpl.getAvailableIngredients] is called
     * Then - repository returns [ApiResponse.Success] with correct data
     */
    @Test
    fun repositoryGetIngredientsReturnsCorrectDataOnSuccessResponse() {
        val expectedResponseData = IngredientsWithRecipesEntity(
            availableIngredients = listOf("Meat", "parmesan", "onion"),
            recipes = listOf(
                TestHelper.getRecipeEntityByName("Meatball")
            )
        )
        coEvery { ingredientsRemoteDataSource.getAvailableIngredients() } returns Resource.Success(
            expectedResponseData.availableIngredients
        )
        coEvery { recipesCacheDataSource.getCompatibleRecipes(any()) } returns expectedResponseData.recipes

        runTest {
            val response =
                recipeIngredientsRepositoryImpl.getAvailableIngredients(isRefresh = true)
            assertEquals(
                expectedResponseData,
                (response as Resource.Success<*>).data
            )
        }
    }

    /**
     * Given - [IngredientsRemoteDataSource.getAvailableIngredients] return [Resource.Error]
     * When - [RecipeIngredientsRepositoryImpl.getAvailableIngredients] is called
     * Then - [Resource.Error] is returned
     */
    @Test
    fun repositoryGetIngredientsReturnsErrorResponse() {
        coEvery { ingredientsRemoteDataSource.getAvailableIngredients() } returns Resource.Error(
            "Unknown error"
        )
        runTest {
            val response = recipeIngredientsRepositoryImpl.getAvailableIngredients(isRefresh = true)
            assertIs<Resource.Error>(response)
        }
    }

    /**
     * Given - [IngredientsRemoteDataSource.getAvailableIngredients] return [Resource.Empty]
     * When - [RecipeIngredientsRepositoryImpl.getAvailableIngredients] is called
     * Then - [Resource.Empty] is returned
     */
    @Test
    fun repositoryGetIngredientsReturnsCacheEmptyResponse() {
        coEvery { ingredientsRemoteDataSource.getAvailableIngredients() } returns Resource.Empty
        runTest {
            val response = recipeIngredientsRepositoryImpl.getAvailableIngredients(isRefresh = true)
            assertIs<Resource.Empty>(response)
        }
    }
    //endregion


    /**
     * Given - [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Success] with data
     * & [RecipesCacheDataSource.getCompatibleRecipes] returns correct [RecipeData] list
     * When - [RecipeIngredientsRepositoryImpl.getAvailableIngredients] is called
     * Then - [ApiResponse.Success] is returned
     */
    @Test
    fun repositoryGetIngredientsReturnsCacheSuccessResponse() {
        val availableIngredients = listOf("Meat", "parmesan", "onion")
        coEvery { ingredientsCacheDataSource.getAvailableIngredients() } answers {
            Resource.Success(
                availableIngredients
            )
        }
        coEvery { recipesCacheDataSource.getCompatibleRecipes(any()) } returns listOf(
            requireNotNull(TestHelper.getRecipeEntityByName("Meatball"))
        )
        runTest {
            val response =
                recipeIngredientsRepositoryImpl.getAvailableIngredients(isRefresh = false)
            assertIs<Resource.Success<List<String>>>(response)
        }
    }

    /**
     * Given - [IngredientsCacheDataSource.getAvailableIngredients] returns [Resource.Success] with data
     * & [RecipesCacheDataSource.getCompatibleRecipes] returns correct [RecipeData] list
     * When - [RecipeIngredientsRepositoryImpl.getAvailableIngredients] is called
     * Then - [ApiResponse.Success] is returned with correct data
     */
    @Test
    fun repositoryGetIngredientsReturnsCorrectCachedDataOnSuccessResponse() {
        val expectedResponseData = IngredientsWithRecipesEntity(
            availableIngredients = listOf("Meat", "parmesan", "onion"),
            recipes = listOf(
                TestHelper.getRecipeEntityByName("Meatball")
            )
        )
        coEvery { ingredientsCacheDataSource.getAvailableIngredients() } answers {
            Resource.Success(
                expectedResponseData.availableIngredients
            )
        }
        coEvery { recipesCacheDataSource.getCompatibleRecipes(any()) } returns expectedResponseData.recipes

        runTest {
            val response =
                recipeIngredientsRepositoryImpl.getAvailableIngredients(isRefresh = false)
            assertEquals(
                expectedResponseData,
                (response as Resource.Success<*>).data
            )
        }
    }

    // region Removing an ingredient


    /**
     * Given - [IngredientsCacheDataSource.removeIngredient] returns correct data
     * & [RecipesCacheDataSource.getCompatibleRecipes] returns correct [RecipeData] list
     * When - [RecipeIngredientsRepositoryImpl.removeIngredient] is called
     * Then - [ApiResponse.Success] is returned with correct data
     */
    @Test
    fun repositoryRemoveIngredientReturnsCorrectCachedDataOnSuccessResponse() {
        val expectedResponseData = IngredientsWithRecipesEntity(
            availableIngredients = listOf("Meat", "parmesan", "onion"),
            recipes = listOf(
                TestHelper.getRecipeEntityByName("Meatball")
            )
        )
        coEvery { ingredientsCacheDataSource.removeIngredient(any()) } returns
                expectedResponseData.availableIngredients

        coEvery { recipesCacheDataSource.getCompatibleRecipes(any()) } returns expectedResponseData.recipes

        runTest {
            val response =
                recipeIngredientsRepositoryImpl.removeIngredient("chicken")
            assertEquals(
                expectedResponseData,
                (response as Resource.Success<*>).data
            )
        }
    }

    /**
     * Given - [IngredientsCacheDataSource.removeIngredient] returns empty list
     * & [RecipesCacheDataSource.getCompatibleRecipes] returns empty list
     * When - [RecipeIngredientsRepositoryImpl.removeIngredient] is called
     * Then - [Resource.Empty] is returned
     */
    @Test
    fun repositoryRemoveIngredientReturnsCEmptyResponse() {
        coEvery { ingredientsCacheDataSource.removeIngredient(any()) } returns emptyList()
        coEvery { recipesCacheDataSource.getCompatibleRecipes(any()) } returns emptyList()

        runTest {
            val response =
                recipeIngredientsRepositoryImpl.removeIngredient("chicken")
            assertIs<Resource.Empty>(response)
        }
    }
    //endregion


}