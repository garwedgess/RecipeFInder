package eu.wedgess.recipefinder.domain.entities

data class IngredientsWithRecipesEntity(
    val availableIngredients: List<String> = emptyList(),
    val recipes: List<RecipeEntity>
)