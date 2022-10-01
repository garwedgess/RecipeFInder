package eu.wedgess.recipefinder.ui.home

import eu.wedgess.recipefinder.data.model.RecipeData


/**
 * HomeUiState is our UI state which will be used by the UI composables
 */
sealed interface HomeUiState {

    val isLoading: Boolean
    val errorMessage: String?

    /**
     * No recipes
     * Represents the UI state that there is no recipes compatible with the available ingredients
     *
     * @property availableIngredients
     * @property isLoading
     * @property errorMessage
     * @constructor Create empty No recipes
     */
    data class NoRecipes(
        val availableIngredients: List<String>,
        override val isLoading: Boolean = false,
        override val errorMessage: String? = null
    ) : HomeUiState

    /**
     * Has recipes
     * Represents the UI state when there are recipes compatible with the available ingredients
     *
     * @property recipes
     * @property availableIngredients
     * @property isLoading
     * @property errorMessage
     * @constructor Create empty Has recipes
     */
    data class HasRecipes(
        val recipes: List<RecipeData>,
        val availableIngredients: List<String>,
        override val isLoading: Boolean = false,
        override val errorMessage: String? = null
    ) : HomeUiState

    /**
     * No ingredients
     * Represents the UI state when there are no ingredients and therefore no recipes
     *
     * @property isLoading
     * @property errorMessage
     * @constructor Create empty No ingredients
     */
    data class NoIngredients(
        override val isLoading: Boolean = false,
        override val errorMessage: String? = null
    ) : HomeUiState
}

/**
 * Home view model state
 *
 * The [HomeViewModel] state
 *
 * @property recipes - list of recipes to display
 * @property availableIngredients - list of ingredients to display
 * @property errorMessage - error message to display if any
 * @property isLoading - whether or not the screen or data is loading
 * @constructor Create empty Home view model state
 */
data class HomeViewModelState(
    val recipes: List<RecipeData>? = null,
    val availableIngredients: List<String>? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
) {

    /**
     * Function to convert the [HomeViewModelState] to [HomeUiState] which will be consumed by the composables
     *
     * @return instance of [HomeUiState]
     */
    fun toUiState(): HomeUiState =
        if (availableIngredients == null) {
            HomeUiState.NoIngredients(
                isLoading = isLoading,
                errorMessage = errorMessage
            )
        } else if (recipes.isNullOrEmpty()) {
            HomeUiState.NoRecipes(
                isLoading = isLoading,
                errorMessage = errorMessage,
                availableIngredients = availableIngredients
            )
        } else {
            HomeUiState.HasRecipes(
                isLoading = isLoading,
                errorMessage = errorMessage,
                availableIngredients = availableIngredients,
                recipes = recipes
            )
        }
}
