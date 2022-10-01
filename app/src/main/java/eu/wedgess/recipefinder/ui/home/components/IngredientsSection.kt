package eu.wedgess.recipefinder.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import eu.wedgess.recipefinder.R
import eu.wedgess.recipefinder.data.model.AvailableIngredientsData


/**
 * Ingredients section - the section for displaying all of the available ingredients
 *
 * @param titleResourceId - the title string resource ID to display above the ingredients
 * @param availableIngredients - the [AvailableIngredientsData] object which contains the available ingredients list
 * @param onIngredientClicked - action for when an IngredientChip is clicked
 */
@Composable
fun IngredientsSection(
    titleResourceId: Int,
    availableIngredients: List<String>,
    onIngredientClicked: ((String) -> Unit)? = null
) {
    Column(modifier = Modifier.testTag("IngredientSectionTag")) {
        Text(
            text = stringResource(titleResourceId),
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
            modifier = Modifier
                .padding(4.dp)
                .testTag("IngredientSectionRowTag"),
            mainAxisSpacing = 4.dp,
            crossAxisSpacing = 4.dp,
        ) {
            availableIngredients.forEach { ingredient ->
                IngredientChip(
                    text = ingredient,
                    onClick = { clickedIngredient ->
                        onIngredientClicked?.invoke(clickedIngredient)
                    })
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun IngredientsSectionPreview() {
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
}

@Preview(showBackground = true)
@Composable
fun IngredientsSectionWithEmptyDataPreview() {
    IngredientsSection(
        titleResourceId = R.string.title_available_ingredients,
        availableIngredients = emptyList(),
        onIngredientClicked = {}
    )
}