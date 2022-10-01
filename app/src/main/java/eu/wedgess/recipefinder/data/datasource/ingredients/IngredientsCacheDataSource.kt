package eu.wedgess.recipefinder.data.datasource.ingredients

import eu.wedgess.recipefinder.domain.entities.Resource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Singleton

/**
 * Ingredients cache data source
 *
 * Typically we'd store the data locally in a Database using Room or similar.
 * For example purposes the ingredients will be kept in memory
 *
 */
@Singleton
class IngredientsCacheDataSource : IngredientsDataSource {

    // Memory cache of the available ingredients from the network.
    private var availableIngredients: List<String> = emptyList()

    // Used to make suspend functions that read and update state safe to call from any thread
    private val mutex = Mutex()

    /**
     * Get available ingredients from the cached [availableIngredients].
     *
     * @return - If [availableIngredients] is empty return [Resource.Empty]
     * otherwise return [Resource.Success] with the [availableIngredients]
     */
    override suspend fun getAvailableIngredients(): Resource<List<String>> {
        return mutex.withLock {
            if (availableIngredients.isEmpty()) {
                Resource.Empty
            } else {
                Resource.Success(availableIngredients)
            }
        }
    }

    /**
     * Remove ingredient from the cached [availableIngredients]
     *
     * @param ingredient - the ingredient to remove from [availableIngredients]
     * @return - the updated [availableIngredients]
     */
    internal suspend fun removeIngredient(ingredient: String): List<String> {
        return mutex.withLock {
            // update our cache
            availableIngredients = availableIngredients.filter { it != ingredient }
            availableIngredients
        }
    }

    /**
     * Cache all available ingredients
     *
     * @param availableIngredients - ingredients to cache
     */
    internal suspend fun cacheAllAvailableIngredients(availableIngredients: List<String>) {
        mutex.withLock {
            this.availableIngredients = availableIngredients
        }
    }
}