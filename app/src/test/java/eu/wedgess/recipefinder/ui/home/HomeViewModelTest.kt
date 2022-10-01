package eu.wedgess.recipefinder.ui.home

import eu.wedgess.recipefinder.helpers.MainCoroutineRule
import eu.wedgess.recipefinder.helpers.TestHelper
import eu.wedgess.recipefinder.domain.entities.IngredientsWithRecipesEntity
import eu.wedgess.recipefinder.domain.entities.Resource
import eu.wedgess.recipefinder.domain.usecases.GetIngredientsAndRecipesUseCase
import eu.wedgess.recipefinder.domain.usecases.RemoveIngredientUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: HomeViewModel

    @MockK
    private lateinit var getIngredientsAndRecipesUseCase: GetIngredientsAndRecipesUseCase

    @MockK
    private lateinit var removeIngredientUseCase: RemoveIngredientUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = HomeViewModel(getIngredientsAndRecipesUseCase, removeIngredientUseCase)
    }

    @After
    fun teardown() = unmockkAll()

    /**
     * Given - [GetIngredientsAndRecipesUseCase.execute] returns [Resource.Success] with data
     * When - [HomeViewModel.getAvailableIngredients] is called on init
     * Then - [GetIngredientsAndRecipesUseCase.execute] is called exactly once
     */
    @Test
    fun viewModelGetIngredientsCallsUseCaseExactlyOnce() {
        coEvery { getIngredientsAndRecipesUseCase.execute(refresh = any()) } returns Resource.Success(
            IngredientsWithRecipesEntity(
                availableIngredients = listOf("Meat", "onion"),
                recipes = emptyList()
            )
        )
        runTest {
            runCurrent()
        }
        coVerify(exactly = 1) { getIngredientsAndRecipesUseCase.execute(any()) }
    }

    /**
     * Given - [GetIngredientsAndRecipesUseCase.execute] returns [Resource.Success] with data
     * When - [HomeViewModel.getAvailableIngredients] is called on init
     * Then - ui state is [HomeUiState.HasRecipes]
     */
    @Test
    fun viewModelGetIngredientsReturnsStateAsTypeHasRecipes() {
        val expectedResponse = IngredientsWithRecipesEntity(
            availableIngredients = listOf("Meat", "egg", "onion"),
            recipes = TestHelper.getRecipesByName(listOf("Meatloaf"))
        )
        coEvery { getIngredientsAndRecipesUseCase.execute(refresh = any()) } returns Resource.Success(
            expectedResponse
        )
        runTest {
            runCurrent()
            assertIs<HomeUiState.HasRecipes>(viewModel.uiState.value)
        }
    }

    /**
     * Given - [GetIngredientsAndRecipesUseCase.execute] returns [Resource.Error]
     * When - [HomeViewModel.getAvailableIngredients] is called on init
     * Then - ui state is [HomeUiState.NoIngredients]
     */
    @Test
    fun viewModelGetIngredientsReturnsStateAsTypeNoIngredients() {
        coEvery { getIngredientsAndRecipesUseCase.execute(refresh = any()) } returns Resource.Error(
            "Unexpected error"
        )
        runTest {
            runCurrent()
            assertIs<HomeUiState.NoIngredients>(viewModel.uiState.value)
        }
    }

    /**
     * Given - [GetIngredientsAndRecipesUseCase.execute] returns [Resource.Empty]
     * When - [HomeViewModel.getAvailableIngredients] is called on init
     * Then - ui state is of type [HomeUiState.NoRecipes]
     */
    @Test
    fun viewModelGetIngredientsReturnsStateAsTypeNoIngredientsWhenEmpty() {
        coEvery { getIngredientsAndRecipesUseCase.execute(any()) } returns Resource.Empty
        runTest {
            runCurrent()
            assertIs<HomeUiState.NoIngredients>(viewModel.uiState.value)
        }
    }

    /**
     * Given - [GetIngredientsAndRecipesUseCase.execute] returns [Resource.Success] with data
     * When - [HomeViewModel.getAvailableIngredients] is called on init
     * Then - ui state contains correct recipes for ingredients
     */
    @Test
    fun viewModelGetIngredientsReturnsExpectedRecipesData() {
        val expectedResponse = IngredientsWithRecipesEntity(
            availableIngredients = listOf("Meat", "egg", "onion"),
            recipes = TestHelper.getRecipesByName(listOf("Meatloaf"))
        )
        coEvery { getIngredientsAndRecipesUseCase.execute(refresh = any()) } returns Resource.Success(
            expectedResponse
        )
        runTest {
            runCurrent()
            assertEquals(
                expectedResponse.recipes, // expected Cheesecake & Chocolate brownie
                (viewModel.uiState.value as HomeUiState.HasRecipes).recipes
            )
        }
    }

    /**
     * Given - [GetIngredientsAndRecipesUseCase.execute] returns [Resource.Error]
     * When - [HomeViewModel.getAvailableIngredients] is called on init
     * Then - ui state is [HomeUiState.NoIngredients] with correct error message
     */
    @Test
    fun viewModelGetIngredientsReturnsErrorThenErrorMessageIsCorrect() {
        val expectedErrorMsg = "Internal Server Error"
        coEvery { getIngredientsAndRecipesUseCase.execute(refresh = any()) } returns Resource.Error(
            expectedErrorMsg
        )
        runTest {
            runCurrent()
            assertEquals(
                expectedErrorMsg,
                (viewModel.uiState.value as HomeUiState.NoIngredients).errorMessage
            )
        }
    }

    /**
     * Given - [GetIngredientsAndRecipesUseCase.execute] returns [Resource.Success] with ingredients but no recipes
     * When - [HomeViewModel.getAvailableIngredients] is called on init
     * Then - ui state is of type [HomeUiState.NoRecipes]
     */
    @Test
    fun viewModelGetIngredientsReturnsIngredientsButNoRecipes() {
        coEvery { getIngredientsAndRecipesUseCase.execute(any()) } returns Resource.Success(
            IngredientsWithRecipesEntity(
                availableIngredients = listOf("egg", "onion"),
                recipes = emptyList()
            )
        )
        runTest {
            runCurrent()
            assertIs<HomeUiState.NoRecipes>(viewModel.uiState.value)
        }
    }


    /**
     * Given - [GetIngredientsAndRecipesUseCase.execute] returns [Resource.Success] with data but all recipes is empty
     * and [RemoveIngredientUseCase.execute] returns [Resource.Success] with ingredient removed
     * When - [HomeViewModel.getAvailableIngredients] is called on init and
     * then [HomeViewModel.removeIngredientFromList] is called
     * Then - ui state has ingredient removed
     */
    @Test
    fun viewModelRemoveIngredientFromListReturnsStateWithIngredientRemoved() {
        val availableIngredients = listOf("biscuit", "chocolate", "butter", "sugar")
        val listAfterIngredientIsRemoved =
            availableIngredients.subList(1, availableIngredients.size)
        val ingredientToRemove = availableIngredients[0]
        coEvery { getIngredientsAndRecipesUseCase.execute(any()) } returns Resource.Success(
            IngredientsWithRecipesEntity(
                availableIngredients = availableIngredients,
                recipes = emptyList()
            )
        )
        coEvery { removeIngredientUseCase.execute(any()) } returns Resource.Success(
            IngredientsWithRecipesEntity(
                availableIngredients = listAfterIngredientIsRemoved,
                recipes = emptyList()
            )
        )
        runTest {
            runCurrent()
            viewModel.removeIngredientFromList(ingredientToRemove)
            runCurrent()
            assertEquals(
                listAfterIngredientIsRemoved,
                (viewModel.uiState.value as HomeUiState.NoRecipes).availableIngredients
            )
        }
    }

}