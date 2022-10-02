package eu.wedgess.recipefinder.ui.details

import eu.wedgess.recipefinder.domain.entities.RecipeEntity


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
     * @property recipe - the recipe to display
     * @property isLoading
     * @property errorMessage
     * @constructor Create empty Has recipes
     */
    data class RecipeDetails(
        val recipe: RecipeEntity,
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
 * @property recipe - the recipe to display in the UI
 * @property errorMessage
 * @property isLoading
 */
data class DetailsViewModelState(
    val recipe: RecipeEntity? = null,
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
