package eu.wedgess.recipefinder.ui.common

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import eu.wedgess.recipefinder.ui.navigation.Screen
import eu.wedgess.recipefinder.ui.navigation.shouldDisplayNavigationBackButton

@Composable
fun MainAppBar(screen: Screen, navController: NavHostController) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = screen.title)
            )
        },
        navigationIcon = if (navController.previousBackStackEntry != null
            && screen.shouldDisplayNavigationBackButton()
        ) {
            {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Action back"
                    )
                }
            }
        } else null,
    )
}