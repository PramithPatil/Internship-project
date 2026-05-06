package com.example.surya_shaktisolarmonitor.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Surya-Shakti Solar Monitor — Custom Dark Theme
 *
 * Uses a high-contrast Yellow + Black color scheme designed for:
 * - Outdoor readability in direct sunlight
 * - Modern, premium appearance
 * - Material Design 3 compliance
 *
 * The app always uses dark theme for maximum contrast with yellow accents.
 */
private val SuryaShaktiColorScheme = darkColorScheme(
    primary = SolarYellow,
    onPrimary = SolarBlack,
    primaryContainer = SolarYellowDark,
    onPrimaryContainer = SolarWhite,
    secondary = SolarAmber,
    onSecondary = SolarBlack,
    secondaryContainer = SolarGold,
    onSecondaryContainer = SolarBlack,
    tertiary = SolarGreen,
    onTertiary = SolarWhite,
    tertiaryContainer = SolarGreenLight,
    onTertiaryContainer = SolarBlack,
    error = SolarRed,
    onError = SolarWhite,
    background = SolarBlack,
    onBackground = SolarWhite,
    surface = SolarDarkGray,
    onSurface = SolarWhite,
    surfaceVariant = SolarMediumGray,
    onSurfaceVariant = SolarOffWhite,
    outline = SolarLightGray,
    outlineVariant = SolarLightGray
)

@Composable
fun SuryaShaktiSolarMonitorTheme(
    // Always use dark theme for high-contrast solar monitoring
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = SuryaShaktiColorScheme

    // Set system bar colors to match the theme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = SolarBlack.toArgb()
            window.navigationBarColor = SolarBlack.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SuryaShaktiTypography,
        content = content
    )
}