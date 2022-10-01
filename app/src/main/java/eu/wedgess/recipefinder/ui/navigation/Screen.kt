package eu.wedgess.recipefinder.ui.navigation

import eu.wedgess.recipefinder.R

sealed class Screen(
    val title: Int,
    val route: String
) {

    object Home : Screen(R.string.screen_title_home, NavigationKeys.Route.RECIPES_HOME)
    object Details : Screen(R.string.screen_title_details, NavigationKeys.Route.RECIPE_DETAILS)
}

fun Screen.shouldDisplayNavigationBackButton(): Boolean = this is Screen.Details
