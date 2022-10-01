package eu.wedgess.recipefinder.domain.usecases

import eu.wedgess.recipefinder.helpers.TestHelper
import eu.wedgess.recipefinder.domain.RecipeIngredientsRepository
import eu.wedgess.recipefinder.domain.entities.IngredientsWithRecipesEntity
import eu.wedgess.recipefinder.domain.entities.Resource
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
 * This class tests the [GetIngredientsAndRecipesUseCase] and that the expected [Resource] is retuurned
 *
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GetIngredientsAndRecipesUseCaseTest {

    private lateinit var getIngredientsAndRecipesUseCase: GetIngredientsAndRecipesUseCase

    @MockK
    private lateinit var recipeIngredientsRepository: RecipeIngredientsRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getIngredientsAndRecipesUseCase = GetIngredientsAndRecipesUseCase(
            recipeIngredientsRepository
        )
    }

    @After
    fun teardown() = unmockkAll()


    /**
     * Given - [RecipeIngredientsRepository.getAvailableIngredients] returns [Resource.Success] with data
     * When - [GetIngredientsAndRecipesUseCase.execute] is called
     * Then - [RecipeIngredientsRepository.getAvailableIngredients] is executed exactly once
     */
    @Test
    fun useCaseExecuteCallsRepositoryGetAvailableIngredientsExactlyOnce() {
        coEvery { recipeIngredientsRepository.getAvailableIngredients() } returns Resource.Success(
            IngredientsWithRecipesEntity(
                availableIngredients = listOf("Meat", "parmesan", "onion", "egg"),
                recipes = TestHelper.getRecipesByName(listOf("Meatball"))
            )
        )
        runTest {
            getIngredientsAndRecipesUseCase.execute(refresh = false)
        }
        coVerify(exactly = 1) {
            recipeIngredientsRepository.getAvailableIngredients(isRefresh = false)
        }
    }

    /**
     * Given - [RecipeIngredientsRepository.getAvailableIngredients] returns [Resource.Success] with data
     * When - [GetIngredientsAndRecipesUseCase.execute] is called
     * Then - [GetIngredientsAndRecipesUseCase.execute] returns [Resource.Success]
     */
    @Test
    fun useCaseExecuteCallsRepositoryGetAvailableIngredientsAndReturnsSuccess() {
        coEvery { recipeIngredientsRepository.getAvailableIngredients() } returns Resource.Success(
            IngredientsWithRecipesEntity(
                availableIngredients = listOf("Meat", "parmesan", "onion", "egg"),
                recipes = TestHelper.getRecipesByName(listOf("Meatball"))
            )
        )
        runTest {
            val response = getIngredientsAndRecipesUseCase.execute(refresh = false)
            assertIs<Resource.Success<IngredientsWithRecipesEntity>>(response)
        }
    }

    /**
     * Given - [RecipeIngredientsRepository.getAvailableIngredients] returns [Resource.Success] with data
     * When - [GetIngredientsAndRecipesUseCase.execute] is called
     * Then - [GetIngredientsAndRecipesUseCase.execute] returns [Resource.Success] with correct data
     */
    @Test
    fun useCaseExecuteCallsRepositoryGetAvailableIngredientsAndReturnsCorrectDataOnSuccess() {
        val expectedResponseData = IngredientsWithRecipesEntity(
            availableIngredients = listOf("Meat", "parmesan", "onion", "egg"),
            recipes = TestHelper.getRecipesByName(listOf("Meatball"))
        )
        coEvery { recipeIngredientsRepository.getAvailableIngredients() } returns Resource.Success(
            expectedResponseData
        )
        runTest {
            val response = getIngredientsAndRecipesUseCase.execute(refresh = false)
            assertEquals(expectedResponseData, (response as Resource.Success<*>).data)
        }
    }

    /**
     * Given - [RecipeIngredientsRepository.getAvailableIngredients] returns [Resource.Empty]
     * When - [GetIngredientsAndRecipesUseCase.execute] is called
     * Then - [GetIngredientsAndRecipesUseCase.execute] returns [Resource.Empty]
     */
    @Test
    fun useCaseExecuteCallsRepositoryGetAvailableIngredientsAndReturnsEmpty() {
        coEvery { recipeIngredientsRepository.getAvailableIngredients() } returns Resource.Empty
        runTest {
            val response = getIngredientsAndRecipesUseCase.execute(refresh = false)
            assertIs<Resource.Empty>(response)
        }
    }

    /**
     * Given - [RecipeIngredientsRepository.getAvailableIngredients] returns [Resource.Error]
     * When - [GetIngredientsAndRecipesUseCase.execute] is called
     * Then - [GetIngredientsAndRecipesUseCase.execute] returns [Resource.Error]
     */
    @Test
    fun useCaseExecuteCallsRepositoryGetAvailableIngredientsAndReturnsError() {
        coEvery { recipeIngredientsRepository.getAvailableIngredients() } returns Resource.Error("Unknown error")
        runTest {
            val response = getIngredientsAndRecipesUseCase.execute(refresh = false)
            assertIs<Resource.Error>(response)
        }
    }

    /**
     * Given - [RecipeIngredientsRepository.getAvailableIngredients] returns [Resource.Error]
     * When - [GetIngredientsAndRecipesUseCase.execute] is called
     * Then - [GetIngredientsAndRecipesUseCase.execute] returns [Resource.Error]
     */
    @Test
    fun useCaseExecuteCallsRepositoryGetAvailableIngredientsAndReturnsErrorThenErrorMessageIsCorrect() {
        val expectedErrorMessage = "Unknown error"
        coEvery { recipeIngredientsRepository.getAvailableIngredients() } returns Resource.Error(
            expectedErrorMessage
        )
        runTest {
            val response = getIngredientsAndRecipesUseCase.execute(refresh = false)
            assertEquals(expectedErrorMessage, (response as Resource.Error).message)
        }
    }
}