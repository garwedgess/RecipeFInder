package eu.wedgess.recipefinder.helpers

import eu.wedgess.recipefinder.data.model.RecipeData
import eu.wedgess.recipefinder.domain.entities.RecipeEntity

object TestHelper {

    fun createAllRecipesDataList(): List<RecipeData> {
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

    fun createAllRecipesEntityList(): List<RecipeEntity> {
        return listOf(
            RecipeEntity(
                name = "Tomato pasta",
                ingredients = listOf("tomato", "pasta", "water")
            ),
            RecipeEntity(
                name = "Chicken tikka masala",
                ingredients = listOf("chicken", "butter", "onion")
            ),
            RecipeEntity(
                name = "Cheesecake",
                ingredients = listOf("biscuit", "butter", "sugar")
            ),
            RecipeEntity(
                name = "Chocolate brownie",
                ingredients = listOf("chocolate", "butter", "sugar")
            ),
            RecipeEntity(
                name = "Meatball",
                ingredients = listOf("Meat", "parmesan", "onion")
            ),
            RecipeEntity(
                name = "Meatloaf",
                ingredients = listOf("Meat", "egg", "onion")
            )
        )
    }

    fun getRecipeDataByName(name: String): RecipeData {
        return requireNotNull(createAllRecipesDataList().first { it.name == name })
    }

    fun getRecipeEntityByName(name: String): RecipeEntity {
        return requireNotNull(createAllRecipesEntityList().first { it.name == name })
    }

    fun getRecipesDataByName(names: List<String>): List<RecipeData> {
        return createAllRecipesDataList().filter { names.contains(it.name) }
    }

    fun getRecipesEntityByName(names: List<String>): List<RecipeEntity> {
        return createAllRecipesEntityList().filter { names.contains(it.name) }
    }

}