package com.example.kubik.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = KubikPrimaryDark,
    background = KubikBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceTextDark,
    onSecondary = textOnBackgroundSecondary,
    outline = OutlinedDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = textOnPrimaryContainerDark,
    secondaryContainer = secondaryContainerDark,
    surfaceVariant = tabContainerDark,
    onSurfaceVariant = OnSurfaceVariantTextDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    onSecondaryContainer = onSecondaryContainerDark
)

private val LightColorScheme = lightColorScheme(
    primary = KubikPrimary,              // основной цвет (кнопки, акценты)
    secondary = StatusGreen,        // вторичный цвет     //
    background = KubikBackground,       // общий фон
    surface = Surface,       // фон карточек
    onPrimary = Color.White,        // текст на основном цвете
    onBackground = TextPrimary,      // текст на общем фоне
    onSecondary = textOnBackgroundSecondary,
    surfaceVariant = tabContainer,
    tertiary = tertiary,
    onTertiary = onTertiary,
    onSurface = OnSurfaceText,
    outline = Outlined,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = textOnPrimaryContainer,
    secondaryContainer = secondaryContainer,
    onSurfaceVariant = OnSurfaceVariantText,
    tertiaryContainer = tertiaryContainer,
    onTertiaryContainer = onTertiaryContainer,
    onSecondaryContainer = onSecondaryContainer
)

val LocalIsDarkTheme = compositionLocalOf { false }


@Composable
fun KubikTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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
    CompositionLocalProvider(LocalIsDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}