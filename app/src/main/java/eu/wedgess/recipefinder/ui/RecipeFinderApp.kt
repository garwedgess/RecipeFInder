package eu.wedgess.recipefinder.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import eu.wedgess.recipefinder.ui.home.HomeScreen
import eu.wedgess.recipefinder.ui.home.HomeViewModel
import eu.wedgess.recipefinder.ui.theme.Amber700


@Composable
fun RecipeFinderApp() {
    val viewModel: HomeViewModel = hiltViewModel()

    val scaffoldState: ScaffoldState = rememberScaffoldState()

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(scaffoldState = scaffoldState) { padding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.isLoading),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    contentColor = Amber700
                )
            },
            onRefresh = {
                viewModel.refresh()
            }) {
            HomeScreen(uiState = uiState, onIngredientClicked = { ingredient ->
                viewModel.removeIngredientFromList(ingredient)
            }, onRecipeClicked = {

            }, onRetry = {
                viewModel.refresh()
            })
        }

    }
}