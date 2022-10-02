package eu.wedgess.recipefinder.domain.entities

import kotlinx.serialization.Serializable

/**
 * Data class representing the recipes
 *
 * @property name - the name of the recipe
 * @property ingredients - the list of ingredients for this recipe
 */
@Serializable
data class RecipeEntity(
    val name: String,
    val ingredients: List<String>
)
