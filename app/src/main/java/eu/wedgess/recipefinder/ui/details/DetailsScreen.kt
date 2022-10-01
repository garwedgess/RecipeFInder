package eu.wedgess.recipefinder.ui.details

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import eu.wedgess.recipefinder.R
import eu.wedgess.recipefinder.ui.common.ErrorSection
import eu.wedgess.recipefinder.ui.home.components.IngredientsSection

@Composable
fun DetailsScreen(uiState: DetailsUiState) {
    when (uiState) {
        is DetailsUiState.RecipeDetails -> {
            Text(
                modifier = Modifier.testTag("RecipeDetailsTitleTag"),
                text = uiState.recipe.name,
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold)
            )
            IngredientsSection(
                titleResourceId = R.string.title_ingredients,
                availableIngredients = uiState.recipe.ingredients,
                showClearChipIcon = false
            )
        }
        is DetailsUiState.NoDetails -> {
            ErrorSection(errorMessage = uiState.errorMessage)
        }
    }

}