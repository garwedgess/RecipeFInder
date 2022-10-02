package eu.wedgess.recipefinder.data.datasource.recipes

import androidx.annotation.VisibleForTesting
import eu.wedgess.recipefinder.data.mappers.RecipeMapper
import eu.wedgess.recipefinder.data.model.RecipeData
import eu.wedgess.recipefinder.domain.entities.RecipeEntity
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Recipes cache data source
 *
 * Typically we'd store the data locally in a Database using Room or similar.
 * For example purposes the recipes will be kept in memory.
 *
 * If the recipes were moved to an API we'd create a remote data source and fetch them from there,
 * but for sample purposes the JSON will be read from a String.
 *
 * @constructor Create empty Recipes cache data source
 */
@Singleton
class RecipesCacheDataSource(val mapper: RecipeMapper) : RecipesDataSource {

    // Used to make suspend functions that read safe to call from any thread
    private val mutex = Mutex()

    // Memory cache all recipes deserializing the JSON
    private val allRecipes by lazy {
        Json.decodeFromString<List<RecipeData>>(RECIPES_JSON)
    }

    override suspend fun getAllRecipes(): List<RecipeEntity> {
        return mutex.withLock { allRecipes.map { mapper.mapToEntity(it) } }
    }

    override suspend fun getCompatibleRecipes(ingredients: List<String>): List<RecipeEntity> {
        return mutex.withLock {
            getRecipesForIngredients(
                allRecipes,
                ingredients
            ).map { mapper.mapToEntity(it) }
        }
    }

    /**
     * Get teh recipes for the available ingredients
     *
     * @return
     */
    private fun getRecipesForIngredients(
        recipes: List<RecipeData>,
        ingredients: List<String>
    ): List<RecipeData> {
        return recipes.filter {
            ingredients.containsAll(it.ingredients)
        }
    }

    companion object {
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        const val RECIPES_JSON = """
            [
              {
                "name": "Tomato pasta",
                "ingredients": [
                  "tomato",
                  "pasta",
                  "water"
                ]
              },
              {
                "name": "Chicken tikka masala",
                "ingredients": [
                  "chicken",
                  "butter",
                  "onion"
                ]
              },
              {
                "name": "Cheesecake",
                "ingredients": [
                  "biscuit",
                  "butter",
                  "sugar"
                ]
              },
              {
                "name": "Chocolate brownie",
                "ingredients": [
                  "chocolate",
                  "butter",
                  "sugar"
                ]
              },
              {
                "name": "Meatball",
                "ingredients": [
                  "Meat",
                  "parmesan",
                  "onion"
                ]
              },
              {
                "name": "Meatloaf",
                "ingredients": [
                  "Meat",
                  "egg",
                  "onion"
                ]
              }
            ]
        """
    }

}