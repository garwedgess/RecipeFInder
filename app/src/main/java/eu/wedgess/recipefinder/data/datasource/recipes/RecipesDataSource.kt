package eu.wedgess.recipefinder.data.datasource.recipes

import eu.wedgess.recipefinder.domain.entities.RecipeEntity

interface RecipesDataSource {

    suspend fun getAllRecipes(): List<RecipeEntity>

    suspend fun getCompatibleRecipes(ingredients: List<String>): List<RecipeEntity>

}