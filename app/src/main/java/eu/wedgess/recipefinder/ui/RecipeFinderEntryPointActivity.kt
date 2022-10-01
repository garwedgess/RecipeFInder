package eu.wedgess.recipefinder.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFinderEntryPointActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeFinderApp()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RecipeFinderEntryPointActivityPreview() {
    RecipeFinderApp()
}