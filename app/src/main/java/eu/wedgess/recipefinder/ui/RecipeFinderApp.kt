package eu.wedgess.recipefinder.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import eu.wedgess.recipefinder.ui.common.MainAppBar
import eu.wedgess.recipefinder.ui.navigation.NavigationGraph
import eu.wedgess.recipefinder.ui.navigation.Screen
import eu.wedgess.recipefinder.ui.theme.RecipeFinderTheme


@Composable
fun RecipeFinderApp() {
    val navController = rememberNavController()
    var currentScreen by remember {
        mutableStateOf<Screen>(Screen.Home)
    }

    val scaffoldState = rememberScaffoldState()

    RecipeFinderTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                MainAppBar(
                    screen = currentScreen,
                    navController = navController
                )
            }
        ) {
            NavigationGraph(
                navController = navController,
                setCurrentScreen = { screen ->
                    currentScreen = screen
                }
            )
        }
    }
}