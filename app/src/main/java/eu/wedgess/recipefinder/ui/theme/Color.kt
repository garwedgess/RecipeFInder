package eu.wedgess.recipefinder.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

val Blue200 = Color(0xFF90CAF9)
val Blue500 = Color(0xFF2196F3)
val Blue700 = Color(0xFF0288D1)
val Amber200 = Color(0xFFFFE082)
val Amber700 = Color(0xFFFFA000)
val Grey200 = Color(0xFFF8F8F8)
val Black200 = Color(0xFF121212)

@get:Composable
@get:ReadOnlyComposable
val Colors.ingredientChipBackground: Color
    get() = if (isLight) Color.LightGray.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f)


@get:Composable
@get:ReadOnlyComposable
val Colors.ingredientChipBorder: Color
    get() = if (isLight) Color.LightGray else Color.DarkGray



