package eu.wedgess.recipefinder.ui.details

import androidx.lifecycle.SavedStateHandle
import eu.wedgess.recipefinder.helpers.MainCoroutineRule
import eu.wedgess.recipefinder.data.model.RecipeData
import eu.wedgess.recipefinder.ui.navigation.NavigationKeys
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: DetailsViewModel

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true, relaxed = true)
        viewModel = DetailsViewModel(savedStateHandle)
    }

    @After
    fun teardown() = unmockkAll()

    /**
     * Given - Correct JSON string returned by [SavedStateHandle]
     * When - [DetailsViewModel] is initialized
     * Then - [SavedStateHandle.get] with correct argument is called exactly once
     */
    @Test
    fun viewModelIsInitializedAndSavedStateHandleGetStringIsCalledExactlyOnce() {
        every { savedStateHandle.get<String>(NavigationKeys.Arg.SELECTED_RECIPE) } returns "{\"name\":\"Chicken tikka masala\",\"ingredients\":[\"chicken\",\"butter\",\"onion\"]}}"
        runTest {
            runCurrent()
            coVerify(exactly = 1) { savedStateHandle.get<String>(NavigationKeys.Arg.SELECTED_RECIPE) }
        }
    }

    /**
     * Given - Malformed JSON returned from [SavedStateHandle]
     * When - [DetailsViewModel] is initialized
     * Then - ui state is [DetailsUiState.NoDetails]
     */
    @Test
    fun viewModelIsInitializedAndGetRecipeWithInvalidJsonSetsStateAsNoDetails() {
        every { savedStateHandle.get<String>(NavigationKeys.Arg.SELECTED_RECIPE) } returns "{\"name\"\"Chicken tikka masala\",\"ingredients\":[\"chicken\",\"butter\",\"onion\"]}}"
        runTest {
            runCurrent()
            assertIs<DetailsUiState.NoDetails>(viewModel.uiState.value)
        }
    }

    /**
     * Given - Malformed JSON returned from [SavedStateHandle]
     * When - [DetailsViewModel] is initialized
     * Then - ui state contains correct error message
     */
    @Test
    fun iewModelIsInitializedAndGetRecipeWithInvalidJsonSetsSateWithJsonErrorMessage() {
        every { savedStateHandle.get<String>(NavigationKeys.Arg.SELECTED_RECIPE) } returns "{\"name\"\"Chicken tikka masala\",\"ingredients\":[\"chicken\",\"butter\",\"onion\"]}}"
        runTest {
            runCurrent()
            assert(requireNotNull(viewModel.uiState.value.errorMessage).contains("Unexpected JSON token at offset"))
        }
    }

    /**
     * Given - Correct JSON string returned from [SavedStateHandle]
     * When - [DetailsViewModel] is initialized
     * Then - ui state is [DetailsUiState.RecipeDetails]
     */
    @Test
    fun iewModelIsInitializedAndGetRecipeWithInvalidJsonSetsStateAsRecipeDetails() {
        every { savedStateHandle.get<String>(NavigationKeys.Arg.SELECTED_RECIPE) } returns "{\"name\":\"Chicken tikka masala\",\"ingredients\":[\"chicken\",\"butter\",\"onion\"]}"
        runTest {
            runCurrent()
            assertIs<DetailsUiState.RecipeDetails>(viewModel.uiState.value)
        }
    }

    /**
     * Given - Correct JSON string returned from [SavedStateHandle]
     * When - [DetailsViewModel] is initialized
     * Then - ui state is [DetailsUiState.RecipeDetails] with correct recipe
     */
    @Test
    fun iewModelIsInitializedAndGetRecipeWithInvalidJsonSetsStateAsRecipeDetailsWithCorrectData() {
        val expectedData = RecipeData(
            name = "Chicken tikka masala",
            ingredients = listOf("chicken", "butter", "onion")
        )
        every { savedStateHandle.get<String>(NavigationKeys.Arg.SELECTED_RECIPE) } returns "{\"name\":\"Chicken tikka masala\",\"ingredients\":[\"chicken\",\"butter\",\"onion\"]}"
        runTest {
            runCurrent()
            assertEquals(
                expectedData,
                (viewModel.uiState.value as DetailsUiState.RecipeDetails).recipe
            )
        }
    }

}