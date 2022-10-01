package eu.wedgess.recipefinder.ui.home

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.wedgess.recipefinder.data.model.AvailableIngredientsData
import eu.wedgess.recipefinder.data.model.RecipeData
import eu.wedgess.recipefinder.ui.home.HomeScreen
import eu.wedgess.recipefinder.ui.home.HomeUiState
import eu.wedgess.recipefinder.ui.theme.RecipeFinderTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    /**
     * Given - [HomeScreen] is loaded
     * When - UI state is [HomeUiState.HasRecipes]
     * Then - [IngredientsSection] has correct number of children, with the correct title and correct number of ingredients
     */
    @Test
    fun givenRecipesAndIngredientsWhenRunThenIngredientsSectionExistsWithCorrectChildCountAndTitleAndNumberOfIngredients() {
        composeTestRule.setContent {
            RecipeFinderTheme {
                HomeScreen(
                    uiState = HomeUiState.HasRecipes(
                        availableIngredients = listOf("biscuit", "butter", "sugar", "chocolate"),
                        recipes = listOf(
                            RecipeData(
                                name = "Cheesecake",
                                ingredients = listOf("biscuit", "butter", "sugar")
                            ),
                            RecipeData(
                                name = "Chocolate brownie",
                                ingredients = listOf("chocolate", "butter", "sugar")
                            ),
                            RecipeData(
                                name = "Chicken tikka masala",
                                ingredients = listOf("chicken", "butter", "onion")
                            )
                        )
                    ),
                    onIngredientClicked = {},
                    onRecipeClicked = {},
                    onRetry = {}
                )
            }
        }
        runTest {
            composeTestRule.onNodeWithTag("IngredientSectionTag")
                .assertExists("Ingredients Section does not exist")
                .onChildren()
                .assertCountEquals(2) // Title & Row
                .onFirst() // Title
                .assert(hasText("Available Ingredients"))
                .onParent() // Section
                .onChildren()
                .onLast() // ingredients row
                .onChildren()
                .assertCountEquals(4) // 4 ingredients
        }
    }

    /**
     * Given - [HomeScreen] is loaded
     * When - UI state is [HomeUiState.HasRecipes]
     * Then - [RecipesListSection] has correct number of children, with correct item text
     */
    @Test
    fun givenRecipesAndIngredientsWhenRunThenRecipesSectionExistsWithCorrectChildCountAndListData() {
        composeTestRule.setContent {
            RecipeFinderTheme {
                HomeScreen(
                    uiState = HomeUiState.HasRecipes(
                        availableIngredients = listOf("biscuit", "butter", "sugar", "chocolate"),
                        recipes = listOf(
                            RecipeData(
                                name = "Cheesecake",
                                ingredients = listOf("biscuit", "butter", "sugar")
                            ),
                            RecipeData(
                                name = "Chocolate brownie",
                                ingredients = listOf("chocolate", "butter", "sugar")
                            )
                        )
                    ),
                    onIngredientClicked = {},
                    onRecipeClicked = {},
                    onRetry = {}
                )
            }
        }
        runTest {
            composeTestRule.onNodeWithTag("RecipesListTag")
                .assertExists("Recipes Section does not exist")
                .onChildren()
                .assertCountEquals(4) // ingredients section, the recipes list title and 2 items
                .onLast()
                .assert(hasText("Chocolate brownie"))
                .onSiblings()[2] // first item
                .assert(hasText("Cheesecake"))
        }
    }

    /**
     * Given - [HomeScreen] is loaded
     * When - UI state is [HomeUiState.NoRecipesOrIngredients]
     * Then - [ErrorSection] has correct error text
     */
    @Test
    fun givenNoRecipesOrIngredientsWhenRunThenErrorMessageIsDisplayedWithCorrectText() {
        val errorMsg = "Unknown error"
        composeTestRule.setContent {
            RecipeFinderTheme {
                HomeScreen(
                    uiState = HomeUiState.NoIngredients(
                        errorMessage = errorMsg,
                        isLoading = false
                    ),
                    onIngredientClicked = {},
                    onRecipeClicked = {},
                    onRetry = {}
                )
            }
        }
        runTest {
            composeTestRule.onNodeWithTag("ErrorMessageTag")
                .assertExists("Error message does not exist")
                .assert(hasText(errorMsg))
        }
    }

    /**
     * Given - [HomeScreen] is loaded
     * When - UI state is [HomeUiState.NoRecipesOrIngredients]
     * Then - [ErrorSection] displays retry button with correct text
     */
    @Test
    fun givenNoRecipesOrIngredientsWhenRunThenRetryButtonIsDisplayedWithCorrectText() {
        composeTestRule.setContent {
            RecipeFinderTheme {
                HomeScreen(
                    uiState = HomeUiState.NoIngredients(
                        errorMessage = "Unknown error",
                        isLoading = false
                    ),
                    onIngredientClicked = {},
                    onRecipeClicked = {},
                    onRetry = {}
                )
            }
        }
        runTest {
            composeTestRule.onNodeWithTag("ErrorRetryButtonTag")
                .assertExists("Retry button does not exist")
                .assert(hasText("Retry"))
        }
    }

    /**
     * Given - [HomeScreen] is loaded
     * When - UI state is [HomeUiState.NoRecipes]
     * Then - [IngredientsSection] has correct number of children, with the correct title and correct number of ingredients
     */
    @Test
    fun givenNoRecipesWhenRunThenIngredientsSectionExistsWithCorrectChildCountAndTitleAndNumberOfIngredients() {
        composeTestRule.setContent {
            RecipeFinderTheme {
                HomeScreen(
                    uiState = HomeUiState.NoRecipes(
                        availableIngredients = listOf("biscuit", "butter", "sugar", "chocolate"),
                        errorMessage = "Unknown error",
                        isLoading = false
                    ),
                    onIngredientClicked = {},
                    onRecipeClicked = {},
                    onRetry = {}
                )
            }
        }
        runTest {
            composeTestRule.onNodeWithTag("IngredientSectionTag")
                .assertExists("Ingredients Section does not exist")
                .onChildren()
                .assertCountEquals(2) // Title & Row
                .onFirst() // Title
                .assert(hasText("Available Ingredients"))
                .onParent() // Section
                .onChildren()
                .onLast() // ingredients row
                .onChildren()
                .assertCountEquals(4) // 4 ingredients
        }
    }

    /**
     * Given - [HomeScreen] is loaded
     * When - UI state is [HomeUiState.NoIngredients]
     * Then - [ErrorSection] has correct error text
     */
    @Test
    fun givenNoIngredientsWhenRunThenNoIngredientsIsDisplayed() {
        composeTestRule.setContent {
            RecipeFinderTheme {
                HomeScreen(
                    uiState = HomeUiState.NoIngredients(
                        errorMessage = null,
                        isLoading = false
                    ),
                    onIngredientClicked = {},
                    onRecipeClicked = {},
                    onRetry = {}
                )
            }
        }
        runTest {
            composeTestRule.onNodeWithTag("ErrorMessageTag")
                .assertExists("Error message does not exist")
                .assert(hasText("No ingredients"))
        }
    }
}