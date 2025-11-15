// ui/theme/Theme.kt
package com.william.yachay_hco.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,          // ← Usa los colores definidos en Color.kt
    secondary = DarkSecondary,      // ← Usa los colores definidos en Color.kt
    tertiary = YellowYachay,        // ← Usa los colores definidos en Color.kt
    background = DarkBackground,    // ← Usa los colores definidos en Color.kt
    onPrimary = DarkOnPrimary,      // ← Usa los colores definidos en Color.kt
    onSecondary = DarkOnSecondary,  // ← Usa los colores definidos en Color.kt
    onBackground = DarkOnBackground,// ← Usa los colores definidos en Color.kt
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,          // ← Usa los colores definidos en Color.kt
    secondary = LightSecondary,      // ← Usa los colores definidos en Color.kt
    tertiary = YellowYachay,         // ← Usa los colores definidos en Color.kt
    background = LightBackground,    // ← Usa los colores definidos en Color.kt
    onPrimary = LightOnPrimary,      // ← Usa los colores definidos en Color.kt
    onSecondary = LightOnSecondary,  // ← Usa los colores definidos en Color.kt
    onBackground = LightOnBackground,// ← Usa los colores definidos en Color.kt
)

@Composable
fun YACHAY_HCOTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}