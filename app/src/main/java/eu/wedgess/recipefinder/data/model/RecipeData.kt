package eu.wedgess.recipefinder.data.model

import kotlinx.serialization.Serializable

/**
 * Data class representing our recipes
 *
 * @property name - the name of the recipe
 * @property ingredients - the list of ingredients for this recipe
 */
@Serializable
data class RecipeData(
    val name: String,
    val ingredients: List<String>
)
