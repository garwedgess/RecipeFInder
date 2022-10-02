package eu.wedgess.recipefinder.ui.details

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
            Divider(color = Color.LightGray.copy(alpha = 0.6f), thickness = 1.dp)
            IngredientsSection(
                sectionModifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
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