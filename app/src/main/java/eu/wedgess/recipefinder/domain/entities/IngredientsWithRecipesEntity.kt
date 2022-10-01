package eu.wedgess.recipefinder.domain.entities

import eu.wedgess.recipefinder.data.model.RecipeData

data class IngredientsWithRecipesEntity(
    val availableIngredients: List<String> = emptyList(),
    val recipes: List<RecipeData>
)