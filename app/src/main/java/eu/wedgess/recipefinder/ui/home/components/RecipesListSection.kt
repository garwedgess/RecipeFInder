package eu.wedgess.recipefinder.ui.home.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.wedgess.recipefinder.R
import eu.wedgess.recipefinder.data.model.RecipeData
import eu.wedgess.recipefinder.domain.entities.RecipeEntity

/**
 * Recipes list section - displays the AvailableIngredientsSection, list title and all compatible recipes in a list
 *
 * @param availableIngredientsSection - the section of available ingredients which we want to display above the list
 * @param recipes - the recipes compatible with the available ingredients
 * @param onRecipeClicked - action for when a recipe is clicked
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecipesListSection(
    availableIngredientsSection: @Composable() (LazyItemScope.() -> Unit),
    recipes: List<RecipeEntity>,
    onRecipeClicked: (RecipeEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .testTag("RecipesListTag")
            .padding(start = 8.dp, end = 8.dp)
    ) {
        item {
            availableIngredientsSection()
        }
        item {
            Text(
                text = stringResource(R.string.title_recipes),
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        items(recipes) { recipe ->
            Card(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    ,
                shape = RoundedCornerShape(2.dp),
                elevation = 4.dp,
                onClick = {
                    onRecipeClicked(recipe)
                }) {

                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                    modifier = Modifier
                        .padding(all = 8.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RecipesListSectionAllDataPreview() {
    RecipesListSection(
        availableIngredientsSection =
        {
            IngredientsSection(
                titleResourceId = R.string.title_available_ingredients,
                availableIngredients = listOf(
                    "Meat",
                    "sugar",
                    "butter",
                    "onion",
                    "milk",
                    "cheese"
                ), onIngredientClicked = {}
            )
        },
        recipes = listOf(
            RecipeEntity(
                name = "MeatLoaf",
                ingredients = listOf("Meat", "onion", "butter")
            )
        ),
        onRecipeClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun RecipesListSectionNoRecipesPreview() {
    RecipesListSection(
        availableIngredientsSection =
        {
            IngredientsSection(
                titleResourceId = R.string.title_available_ingredients,
                availableIngredients = listOf(
                    "Meat",
                    "sugar",
                    "butter",
                    "onion",
                    "milk",
                    "cheese"
                ), onIngredientClicked = {}
            )
        },
        recipes = emptyList(),
        onRecipeClicked = {}
    )
}