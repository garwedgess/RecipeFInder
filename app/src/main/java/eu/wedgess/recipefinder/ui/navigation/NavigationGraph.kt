package eu.wedgess.recipefinder.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.wedgess.recipefinder.ui.details.RecipeDetailsDestination
import eu.wedgess.recipefinder.ui.home.HomeDestination

@Composable
fun NavigationGraph(
    navController: NavHostController,
    setCurrentScreen: (Screen) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavigationKeys.Route.RECIPES_HOME,
        modifier = Modifier.padding(16.dp)
    ) {
        composable(route = NavigationKeys.Route.RECIPES_HOME) {
            setCurrentScreen(Screen.Home)
            HomeDestination(navController)
        }
        composable(
            route = NavigationKeys.Route.RECIPE_DETAILS,
            arguments = listOf(navArgument(NavigationKeys.Arg.SELECTED_RECIPE) {
                type = NavType.StringType
            })
        ) {
            setCurrentScreen(Screen.Details)
            RecipeDetailsDestination()
        }
    }
}