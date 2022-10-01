package eu.wedgess.recipefinder.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.wedgess.recipefinder.R
import eu.wedgess.recipefinder.data.model.RecipeData
import eu.wedgess.recipefinder.ui.common.ErrorSection
import eu.wedgess.recipefinder.ui.home.components.IngredientsSection
import eu.wedgess.recipefinder.ui.home.components.RecipesListSection


/**
 * Home screen composable for displaying the result of the [HomeUiState]
 *
 * @param uiState - instance of [HomeUiState]
 * @param onIngredientClicked - action for when an ingredient is clicked
 * @param onRecipeClicked -  action for when a recipe is clicked
 * @param onRetry -  action for when retry button is clicked
 *
 */
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onIngredientClicked: (String) -> Unit,
    onRecipeClicked: (RecipeData) -> Unit,
    onRetry: () -> Unit
) {
    when (uiState) {
        is HomeUiState.NoRecipes -> {
            // no list so in order for the SwipeRefreshLayout to work we must set vertical scroll
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                IngredientsSection(
                    titleResourceId = R.string.title_available_ingredients,
                    availableIngredients = uiState.availableIngredients,
                    onIngredientClicked = onIngredientClicked
                )
                Text(
                    text = stringResource(R.string.title_no_recipes),
                    style = MaterialTheme.typography.body1.copy(fontSize = 16.sp),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        is HomeUiState.NoIngredients -> {
            // We will have no ingredients or recipes while retrieving ingredients from API
            if (!uiState.isLoading) {
                ErrorSection(
                    errorMessage = uiState.errorMessage
                        ?: stringResource(id = R.string.title_no_ingredients),
                    onRetry = onRetry
                )
            }
        }
        is HomeUiState.HasRecipes -> {
            RecipesListSection(availableIngredientsSection = {
                IngredientsSection(
                    titleResourceId = R.string.title_available_ingredients,
                    availableIngredients = uiState.availableIngredients,
                    onIngredientClicked = onIngredientClicked
                )
            }, recipes = uiState.recipes, onRecipeClicked = onRecipeClicked)

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenNoRecipesPreview() {
    HomeScreen(
        uiState = HomeUiState.NoRecipes(
            availableIngredients = listOf(
                "sugar",
                "eggs"
            )
        ), onIngredientClicked = {}, onRecipeClicked = {}, onRetry = {}
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenNoIngredientsPreview() {
    HomeScreen(
        uiState = HomeUiState.NoIngredients(),
        onIngredientClicked = {},
        onRecipeClicked = {},
        onRetry = {}
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenNoIngredientsWithErrorMsgPreview() {
    HomeScreen(
        uiState = HomeUiState.NoIngredients(errorMessage = "Unexpected error"),
        onIngredientClicked = {},
        onRecipeClicked = {},
        onRetry = {}
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenNoRecipesOrIngredientsPreview() {
    HomeScreen(
        uiState = HomeUiState.HasRecipes(
            availableIngredients = listOf("Meat", "egg", "onion"),
            recipes = listOf(
                RecipeData(
                    name = "Meatloaf",
                    ingredients = listOf("Meat", "egg", "onion")
                )
            )
        ),
        onIngredientClicked = {},
        onRecipeClicked = {},
        onRetry = {}
    )
}