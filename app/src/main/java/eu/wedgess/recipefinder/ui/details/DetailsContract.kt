package eu.wedgess.recipefinder.ui.details

import eu.wedgess.recipefinder.data.model.AvailableIngredientsData
import eu.wedgess.recipefinder.data.model.RecipeData


/**
 * DetailsUiState is our UI state which will be used by the UI composables
 */
sealed interface DetailsUiState {

    val isLoading: Boolean
    val errorMessage: String?

    /**
     * Recipe details
     * Represents the UI state for the selected recipe
     *
     * @property recipes
     * @property availableIngredients
     * @property isLoading
     * @property errorMessage
     * @constructor Create empty Has recipes
     */
    data class RecipeDetails(
        val recipe: RecipeData,
        override val isLoading: Boolean = false,
        override val errorMessage: String? = null
    ) : DetailsUiState

    data class NoDetails(
        override val isLoading: Boolean = false,
        override val errorMessage: String?
    ) : DetailsUiState
}

/**
 * Details view model state
 *
 * The [DetailsViewModel] state
 *
 * @property recipe
 * @property errorMessage
 * @property isLoading
 */
data class DetailsViewModelState(
    val recipe: RecipeData? = null,
    val availableIngredients: AvailableIngredientsData? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
) {

    /**
     * Function to convert the [DetailsViewModelState] to [DetailsUiState] which will be consumed by the composables
     *
     * @return instance of [DetailsUiState]
     */
    fun toUiState(): DetailsUiState =
        if (recipe == null) {
            DetailsUiState.NoDetails(
                errorMessage = errorMessage
            )
        } else {
            DetailsUiState.RecipeDetails(
                recipe = recipe,
                isLoading = isLoading,
                errorMessage = errorMessage
            )
        }
}
