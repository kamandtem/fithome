package com.fithome.app.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * The exact T-shaped architectural surface from the Living Room design:
 * Path: M49 250H354Q398 250 398 294V535C398 600 260 575 260 690V876H140V690C140 575 5 600 5 535V294Q5 250 49 250Z
 * The stem stretches dynamically down to the container's bottom.
 */
class TShapeSurface(
    private val startTopY: Float = 250f,
    private val neckY: Float = 690f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val h = size.height
            val scaleX = size.width / 403f

            moveTo(49f * scaleX, startTopY)
            lineTo(354f * scaleX, startTopY)
            quadraticTo(398f * scaleX, startTopY, 398f * scaleX, 294f)
            lineTo(398f * scaleX, 535f)
            cubicTo(
                398f * scaleX, 600f,
                260f * scaleX, 575f,
                260f * scaleX, neckY
            )
            lineTo(260f * scaleX, h)
            lineTo(140f * scaleX, h)
            lineTo(140f * scaleX, neckY)
            cubicTo(
                140f * scaleX, 575f,
                5f * scaleX, 600f,
                5f * scaleX, 535f
            )
            lineTo(5f * scaleX, 294f)
            quadraticTo(5f * scaleX, startTopY, 49f * scaleX, startTopY)
            close()
        }
        return Outline.Generic(path)
    }
}

object FitShapes {
    // Dock side pills (curved top)
    val DockPill = RoundedCornerShape(topStart = 38.dp, topEnd = 38.dp, bottomEnd = 0.dp, bottomStart = 0.dp)

    // Weather metrics strip behind card (curved bottom)
    val WeatherStrip = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomEnd = 38.dp, bottomStart = 38.dp)

    // TODAY tab notch hanging down
    val TodayNotch = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomEnd = 20.dp, bottomStart = 20.dp)

    // Workouts panel background (curved top)
    val WorkoutsPanel = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp, bottomEnd = 0.dp, bottomStart = 0.dp)

    // Dashboard dock shape
    val DashboardDock = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp, bottomEnd = 0.dp, bottomStart = 0.dp)

    // Standard cards
    val CardShape = RoundedCornerShape(26.dp)

    // Weather main card
    val WeatherCardShape = RoundedCornerShape(40.dp)

    // Device / Mode tabs
    val TabShape = RoundedCornerShape(26.dp)

    // Pill buttons
    val PillShape = RoundedCornerShape(15.dp)

    // Energy / Calories pill
    val EnergyPillOuter = RoundedCornerShape(43.dp)
    val EnergyPillInner = RoundedCornerShape(38.dp)
}
