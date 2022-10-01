package eu.wedgess.recipefinder.helpers

import eu.wedgess.recipefinder.data.model.RecipeData

object TestHelper {

    fun createAllRecipesList(): List<RecipeData> {
        return listOf(
            RecipeData(
                name = "Tomato pasta",
                ingredients = listOf("tomato", "pasta", "water")
            ),
            RecipeData(
                name = "Chicken tikka masala",
                ingredients = listOf("chicken", "butter", "onion")
            ),
            RecipeData(
                name = "Cheesecake",
                ingredients = listOf("biscuit", "butter", "sugar")
            ),
            RecipeData(
                name = "Chocolate brownie",
                ingredients = listOf("chocolate", "butter", "sugar")
            ),
            RecipeData(
                name = "Meatball",
                ingredients = listOf("Meat", "parmesan", "onion")
            ),
            RecipeData(
                name = "Meatloaf",
                ingredients = listOf("Meat", "egg", "onion")
            )
        )
    }

    fun getRecipeByName(name: String): RecipeData {
        return requireNotNull(createAllRecipesList().first { it.name == name })
    }

    fun getRecipesByName(names: List<String>): List<RecipeData> {
        return createAllRecipesList().filter { names.contains(it.name) }
    }

}