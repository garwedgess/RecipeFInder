package eu.wedgess.recipefinder.data.datasource.recipes

import eu.wedgess.recipefinder.data.model.RecipeData

interface RecipesDataSource {

    suspend fun getAllRecipes(): List<RecipeData>

    suspend fun getCompatibleRecipes(ingredients: List<String>): List<RecipeData>

}