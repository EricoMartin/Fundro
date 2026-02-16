package com.basebox.fundro.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = FundroPrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = FundroSecondaryBlue,
    onPrimaryContainer = FundroPrimaryBlueVariant,

    secondary = FundroGreen,
    onSecondary = Color.White,
    secondaryContainer = FundroGreen.copy(alpha = 0.2f),
    onSecondaryContainer = FundroGreen,

    tertiary = FundroOrange,
    onTertiary = Color.White,

    error = FundroRed,
    onError = Color.White,

    background = FundroBackgroundLight,
    onBackground = FundroTextPrimary,

    surface = FundroSurfaceLight,
    onSurface = FundroTextPrimary,
    surfaceVariant = FundroBackgroundLight,
    onSurfaceVariant = FundroTextSecondary,

    outline = FundroGray,
    outlineVariant = FundroGray.copy(alpha = 0.3f)
)

private val DarkColorScheme = darkColorScheme(
    primary = FundroSecondaryBlue,
    onPrimary = FundroPrimaryBlueVariant,
    primaryContainer = FundroPrimaryBlue,
    onPrimaryContainer = Color.White,

    secondary = FundroGreen,
    onSecondary = Color.White,
    secondaryContainer = FundroGreen.copy(alpha = 0.3f),
    onSecondaryContainer = FundroGreen,

    tertiary = FundroOrange,
    onTertiary = Color.White,

    error = FundroRed,
    onError = Color.White,

    background = FundroBackgroundDark,
    onBackground = Color.White,

    surface = FundroSurfaceDark,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF374151),
    onSurfaceVariant = FundroTextSecondary,

    outline = FundroGray,
    outlineVariant = FundroGray.copy(alpha = 0.3f)
)

@Composable
fun FundroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FundroTypography,
        content = content
    )
}