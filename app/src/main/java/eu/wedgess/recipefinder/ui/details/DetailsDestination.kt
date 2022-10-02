package eu.wedgess.recipefinder.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RecipeDetailsDestination() {
    val viewModel: DetailsViewModel = hiltViewModel()

    val scaffoldState: ScaffoldState = rememberScaffoldState()

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
            DetailsScreen(uiState = uiState)
        }

    }
}