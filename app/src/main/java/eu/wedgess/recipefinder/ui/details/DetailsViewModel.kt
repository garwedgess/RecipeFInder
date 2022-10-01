package eu.wedgess.recipefinder.ui.details

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.recipefinder.data.model.RecipeData
import eu.wedgess.recipefinder.ui.navigation.NavigationKeys
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    private lateinit var selectedRecipeJson: String

    private val viewModelState = MutableStateFlow(DetailsViewModelState(isLoading = true))

    // UI state exposed to the UI
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    // handle the exception from the coroutines for both tram forecasts depending on which one is used
    private val getRecipeExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelState.update {
            it.copy(
                recipe = null,
                isLoading = false,
                errorMessage = throwable.localizedMessage ?: throwable.message
            )
        }
    }

    init {
        getRecipe()
    }

    private fun getRecipe() {
        viewModelScope.launch(getRecipeExceptionHandler) {
            selectedRecipeJson = stateHandle.get<String>(NavigationKeys.Arg.SELECTED_RECIPE)
                ?: throw IllegalStateException("No recipe passed to destination.")
            viewModelState.update {
                it.copy(
                    recipe = Json.decodeFromString(RecipeData.serializer(), selectedRecipeJson),
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

}