package com.fithome.app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

private val LightColorScheme = lightColorScheme(
    primary = FitColors.BlueTabEnd,
    secondary = FitColors.PurpleCardEnd,
    tertiary = FitColors.Gold,
    background = FitColors.BgTop,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = FitColors.TextPrimary,
    onSurface = FitColors.TextPrimary
)

val FitHomeBackgroundBrush = Brush.verticalGradient(
    0.0f to FitColors.BgTop,
    0.6f to FitColors.BgMid,
    1.0f to FitColors.BgBottom
)

@Composable
fun FitHomeTheme(
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val currentDensity = LocalDensity.current
    val screenWidthDp = configuration.screenWidthDp.toFloat()
    val scaleFactor = screenWidthDp / 403f

    val customDensity = remember(currentDensity, scaleFactor) {
        Density(
            density = currentDensity.density * scaleFactor,
            fontScale = currentDensity.fontScale
        )
    }

    CompositionLocalProvider(LocalDensity provides customDensity) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = FitTypography
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FitHomeBackgroundBrush)
                    // Keep content above the Android navigation bar (status bar is handled per screen).
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                content()
            }
        }
    }
}
