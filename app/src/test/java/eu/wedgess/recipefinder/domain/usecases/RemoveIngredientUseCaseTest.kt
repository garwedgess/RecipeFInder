package eu.wedgess.recipefinder.domain.usecases

import eu.wedgess.recipefinder.domain.RecipeIngredientsRepository
import eu.wedgess.recipefinder.domain.entities.IngredientsWithRecipesEntity
import eu.wedgess.recipefinder.domain.entities.Resource
import eu.wedgess.recipefinder.helpers.TestHelper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * This class test the [RemoveIngredientUseCase] and that the correct [Resource] is returned
 *
 * @constructor Create empty Remove ingredient use case test
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RemoveIngredientUseCaseTest {

    private lateinit var removeIngredientUseCase: RemoveIngredientUseCase

    @MockK
    private lateinit var recipeIngredientsRepository: RecipeIngredientsRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        removeIngredientUseCase = RemoveIngredientUseCase(
            recipeIngredientsRepository
        )
    }

    @After
    fun teardown() = unmockkAll()


    /**
     * Given - [RecipeIngredientsRepository.removeIngredient] returns [Resource.Success] with data
     * When - [RemoveIngredientUseCase.execute] is called
     * Then - [RecipeIngredientsRepository.removeIngredient] is executed exactly once
     */
    @Test
    fun useCaseExecuteCallsRepositoryRemoveIngredientExactlyOnce() {
        val ingredientToRemove = "egg"
        coEvery { recipeIngredientsRepository.removeIngredient(ingredientToRemove) } returns Resource.Success(
            IngredientsWithRecipesEntity(
                availableIngredients = listOf("Meat", "parmesan", "onion"),
                recipes = TestHelper.getRecipesEntityByName(listOf("Meatball"))
            )
        )
        runTest {
            removeIngredientUseCase.execute(ingredient = ingredientToRemove)
        }
        coVerify(exactly = 1) {
            recipeIngredientsRepository.removeIngredient(ingredient = ingredientToRemove)
        }
    }

    /**
     * Given - [RecipeIngredientsRepository.removeIngredient] returns [Resource.Success] with data
     * When - [RemoveIngredientUseCase.execute] is called
     * Then - [RecipeIngredientsRepository.removeIngredient] returns [Resource.Success]
     */
    @Test
    fun useCaseExecuteCallsRepositoryRemoveIngredientAndReturnsSuccess() {
        val ingredientToRemove = "egg"
        coEvery { recipeIngredientsRepository.removeIngredient(ingredientToRemove) } returns Resource.Success(
            IngredientsWithRecipesEntity(
                availableIngredients = listOf("Meat", "parmesan", "onion"),
                recipes = TestHelper.getRecipesEntityByName(listOf("Meatball"))
            )
        )
        runTest {
            val response = removeIngredientUseCase.execute(ingredient = ingredientToRemove)
            assertIs<Resource.Success<IngredientsWithRecipesEntity>>(response)
        }
    }

    /**
     * Given - [RecipeIngredientsRepository.removeIngredient] returns [Resource.Success] with data
     * When - [RemoveIngredientUseCase.execute] is called
     * Then - [RecipeIngredientsRepository.removeIngredient] returns [Resource.Success] with correct data
     */
    @Test
    fun useCaseExecuteCallsRepositoryRemoveIngredientAndReturnsCorrectDataOnSuccess() {
        val expectedResponseData = IngredientsWithRecipesEntity(
            availableIngredients = listOf("Meat", "parmesan", "onion"),
            recipes = TestHelper.getRecipesEntityByName(listOf("Meatball"))
        )
        val ingredientToRemove = "egg"
        coEvery { recipeIngredientsRepository.removeIngredient(ingredientToRemove) } returns Resource.Success(
            expectedResponseData
        )
        runTest {
            val response = removeIngredientUseCase.execute(ingredient = ingredientToRemove)
            assertEquals(expectedResponseData, (response as Resource.Success<*>).data)
        }
    }

    /**
     * Given - [RecipeIngredientsRepository.removeIngredient] returns [Resource.Empty]
     * When - [RemoveIngredientUseCase.execute] is called
     * Then - [RecipeIngredientsRepository.removeIngredient] returns [Resource.Empty]
     */
    @Test
    fun useCaseExecuteCallsRepositoryRemoveIngredientAndReturnsEmpty() {
        val ingredientToRemove = "egg"
        coEvery { recipeIngredientsRepository.removeIngredient(ingredient = ingredientToRemove) } returns Resource.Empty
        runTest {
            val response = removeIngredientUseCase.execute(ingredient = ingredientToRemove)
            assertIs<Resource.Empty>(response)
        }
    }

    /**
     * Given - [RecipeIngredientsRepository.removeIngredient] returns [Resource.Error]
     * When - [RemoveIngredientUseCase.execute] is called
     * Then - [RecipeIngredientsRepository.removeIngredient] returns [Resource.Error]
     */
    @Test
    fun useCaseExecuteCallsRepositoryRemoveIngredientAndReturnsError() {
        val ingredientToRemove = "egg"
        coEvery { recipeIngredientsRepository.removeIngredient(ingredient = ingredientToRemove) } returns Resource.Error(
            "Unknown error"
        )
        runTest {
            val response = removeIngredientUseCase.execute(ingredient = ingredientToRemove)
            assertIs<Resource.Error>(response)
        }
    }

    /**
     * Given - [RecipeIngredientsRepository.removeIngredient] returns [Resource.Error]
     * When - [RemoveIngredientUseCase.execute] is called
     * Then - [RecipeIngredientsRepository.removeIngredient] returns [Resource.Error] with correct error message
     */
    @Test
    fun useCaseExecuteCallsRepositoryRemoveIngredientAndReturnsErrorWithCorrectErrorMessage() {
        val ingredientToRemove = "egg"
        val expectedErrorMsg = "Unknown error"
        coEvery { recipeIngredientsRepository.removeIngredient(ingredient = ingredientToRemove) } returns Resource.Error(
            expectedErrorMsg
        )
        runTest {
            val response = removeIngredientUseCase.execute(ingredient = ingredientToRemove)
            assertEquals(expectedErrorMsg, (response as Resource.Error).message)
        }
    }
}