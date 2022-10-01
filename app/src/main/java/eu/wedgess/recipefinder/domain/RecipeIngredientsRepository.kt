package eu.wedgess.recipefinder.domain

import eu.wedgess.recipefinder.domain.entities.IngredientsWithRecipesEntity
import eu.wedgess.recipefinder.domain.entities.Resource


interface RecipeIngredientsRepository {

    suspend fun getAvailableIngredients(isRefresh: Boolean = false): Resource<IngredientsWithRecipesEntity>

    suspend fun removeIngredient(ingredient: String): Resource<IngredientsWithRecipesEntity>

}