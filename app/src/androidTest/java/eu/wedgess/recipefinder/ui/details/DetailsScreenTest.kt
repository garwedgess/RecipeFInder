package eu.wedgess.recipefinder.ui.details

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.wedgess.recipefinder.data.model.RecipeData
import eu.wedgess.recipefinder.domain.entities.RecipeEntity
import eu.wedgess.recipefinder.ui.theme.RecipeFinderTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    /**
     * Given - [DetailsScreen] is loaded
     * When - UI state is [DetailsUiState.RecipeDetails]
     * Then - Recipe title exists with correct text
     */
    @Test
    fun givenRecipesDetailsStateWhenRunThenRecipeTitleIsCorrect() {
        composeTestRule.setContent {
            RecipeFinderTheme {
                DetailsScreen(
                    uiState = DetailsUiState.RecipeDetails(
                        recipe = RecipeEntity(
                            name = "Cheesecake",
                            ingredients = listOf("biscuit", "butter", "sugar")
                        ),
                        isLoading = false,
                        errorMessage = null
                    )
                )
            }
        }
        runTest {
            composeTestRule.onNodeWithTag("RecipeDetailsTitleTag")
                .assertExists("Recipe Title does not exist")
                .assert(hasText("Cheesecake"))
        }
    }

    /**
     * Given - [DetailsScreen] is loaded
     * When - UI state is [DetailsUiState.RecipeDetails]
     * Then - [IngredientsSection] exists with correct text, correct child count and ingredients section has correct number of ingredients
     */
    @Test
    fun givenRecipesDetailsStateWhenRunThenRecipeIngredientsHasCorrectNumberOfIngredients() {
        composeTestRule.setContent {
            RecipeFinderTheme {
                DetailsScreen(
                    uiState = DetailsUiState.RecipeDetails(
                        recipe = RecipeEntity(
                            name = "Cheesecake",
                            ingredients = listOf("biscuit", "butter", "sugar")
                        ),
                        isLoading = false,
                        errorMessage = null
                    )
                )
            }
        }
        runTest {
            composeTestRule.onNodeWithTag("IngredientSectionTag")
                .assertExists("Ingredients Section does not exist")
                .onChildren()
                .onFirst()
                .assert(hasText("Ingredients"))
                .onParent()
                .onChildren()
                .assertCountEquals(2) // title + ingredients row
                .onLast() // ingredients row
                .onChildren() // ingredient chips
                .assertCountEquals(3)
        }
    }

    /**
     * Given - [DetailsScreen] is loaded
     * When - UI state is [DetailsUiState.NoDetails]
     * Then - [ErrorSection] has correct error text
     */
    @Test
    fun givenNoDetailsWhenRunThenErrorMessageIsDisplayedWithCorrectText() {
        val errorMsg = "Unknown error"
        composeTestRule.setContent {
            RecipeFinderTheme {
                DetailsScreen(
                    uiState = DetailsUiState.NoDetails(
                        errorMessage = errorMsg,
                        isLoading = false
                    )
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
     * Given - [DetailsScreen] is loaded
     * When - UI state is [DetailsUiState.NoDetails]
     * Then - [ErrorSection] does not display retry button
     */
    @Test
    fun givenNoDetailsWhenRunThenRetryButtonIsDisplayedWithCorrectText() {
        composeTestRule.setContent {
            RecipeFinderTheme {
                DetailsScreen(
                    uiState = DetailsUiState.NoDetails(
                        errorMessage = "Unknown error",
                        isLoading = false
                    )
                )
            }
        }
        runTest {
            composeTestRule.onNodeWithTag("ErrorRetryButtonTag")
                .assertDoesNotExist()
        }
    }
}