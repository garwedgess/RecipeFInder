package eu.wedgess.recipefinder.ui.home

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
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import eu.wedgess.recipefinder.data.model.RecipeData
import eu.wedgess.recipefinder.domain.entities.RecipeEntity
import eu.wedgess.recipefinder.ui.navigation.NavigationKeys
import eu.wedgess.recipefinder.ui.theme.Amber700
import kotlinx.serialization.json.Json

@Composable
fun HomeDestination(navController: NavController) {
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
                navController.navigate(
                    "${NavigationKeys.Route.RECIPES_HOME}/${
                        Json.encodeToString(RecipeEntity.serializer(), it)
                    }"
                )
            }, onRetry = {
                viewModel.refresh()
            })
        }

    }
}