package eu.wedgess.recipefinder.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.recipefinder.domain.entities.IngredientsWithRecipesEntity
import eu.wedgess.recipefinder.domain.entities.Resource
import eu.wedgess.recipefinder.domain.usecases.GetIngredientsAndRecipesUseCase
import eu.wedgess.recipefinder.domain.usecases.RemoveIngredientUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Home view model for fetching/removing ingredients and managing the UI state
 *
 * @property getIngredientsAndRecipesUseCase - for fetching ingredients from network/cache
 * @property removeIngredientUseCase - remove ingredient from cache
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getIngredientsAndRecipesUseCase: GetIngredientsAndRecipesUseCase,
    private val removeIngredientUseCase: RemoveIngredientUseCase
) :
    ViewModel() {

    private val viewModelState = MutableStateFlow(HomeViewModelState(isLoading = true))

    // UI state exposed to the UI
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )


    // handle any exceptions thrown while executing the coroutine for fetching available ingredients
    private val getAvailableIngredientsExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            viewModelState.update {
                it.copy(
                    recipes = null,
                    availableIngredients = null,
                    errorMessage = throwable.localizedMessage ?: throwable.message,
                    isLoading = false
                )
            }
        }

    // handle any exceptions thrown while executing the coroutine for removing an ingredient
    private val removeIngredientExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            viewModelState.update {
                it.copy(
                    recipes = null,
                    availableIngredients = null,
                    errorMessage = throwable.localizedMessage ?: throwable.message,
                    isLoading = false
                )
            }
        }


    init {
        getAvailableIngredients()
    }


    /**
     * Removes and ingredient [ingredientToRemove] from the list of available ingredients
     *
     * @param ingredientToRemove - the ingredient to remove
     */
    internal fun removeIngredientFromList(ingredientToRemove: String) {
        viewModelScope.launch(removeIngredientExceptionHandler) {
            val deferredRemoveIngredient =
                async { removeIngredientUseCase.execute(ingredientToRemove) }
            handleResponseAndUpdateState(deferredRemoveIngredient.await())
        }
    }


    /**
     * Get available ingredients from use case
     *
     * @param refresh - if true refresh from network, if false get ingredients from cache
     */
    private fun getAvailableIngredients(refresh: Boolean = false) {
        viewModelScope.launch(getAvailableIngredientsExceptionHandler) {
            val deferredAvailableIngredients =
                async { getIngredientsAndRecipesUseCase.execute(refresh) }
            handleResponseAndUpdateState(deferredAvailableIngredients.await())
        }
    }

    /**
     * Handle the response from the use case and update the state based on the response
     *
     * @param availableIngredientsResponse
     */
    private fun handleResponseAndUpdateState(availableIngredientsResponse: Resource<IngredientsWithRecipesEntity>) {
        viewModelState.update { state ->
            when (availableIngredientsResponse) {
                is Resource.Success -> {
                    state.copy(
                        isLoading = false,
                        availableIngredients = availableIngredientsResponse.data.availableIngredients,
                        recipes = availableIngredientsResponse.data.recipes
                    )
                }
                is Resource.Error -> {
                    state.copy(
                        availableIngredients = null,
                        recipes = null,
                        isLoading = false,
                        errorMessage = availableIngredientsResponse.message
                    )
                }
                is Resource.Empty -> {
                    state.copy(
                        recipes = null,
                        availableIngredients = null
                    )
                }
            }
        }
    }

    /**
     * Refresh - exposed to UI to refresh ingredients from the network
     *
     */
    internal fun refresh() {
        viewModelState.update { state ->
            state.copy(isLoading = true)
        }
        getAvailableIngredients(refresh = true)
    }
}