package eu.wedgess.recipefinder.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Ingredient chip - used for displaying the individual available ingredients
 *
 * @param text - ingredient name
 * @param showClearIcon - show the clear icon in the chip
 * @param onClick - action for when an ingredient is clicked
 */
@Composable
fun IngredientChip(
    text: String,
    showClearIcon: Boolean = true,
    onClick: (ingredient: String) -> Unit,
) {
    val shape = CircleShape
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = LightGray,
                shape = shape
            )
            .background(
                color = LightGray,
                shape = shape
            )
            .clip(shape = shape)
            .clickable {
                onClick(text)
            }
            .padding(4.dp)
            .testTag("IngredientChipTag")
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            text = text,
            color = Unspecified
        )
        if (showClearIcon) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp, top = 2.dp),
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IngredientChipPreview() {
    IngredientChip(
        text = "sugar",
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun IngredientChipNoIconPreview() {
    IngredientChip(
        text = "sugar",
        showClearIcon = false,
        onClick = {}
    )
}