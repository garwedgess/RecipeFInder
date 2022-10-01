package eu.wedgess.recipefinder.ui.navigation

import eu.wedgess.recipefinder.ui.navigation.NavigationKeys.Arg.SELECTED_RECIPE

object NavigationKeys {

    object Arg {
        const val SELECTED_RECIPE = "recipeJson"
    }

    object Route {
        const val RECIPES_HOME = "recipes_home"
        const val RECIPE_DETAILS = "${RECIPES_HOME}/{$SELECTED_RECIPE}"
    }

}