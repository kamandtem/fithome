package com.fithome.app.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.fithome.app.ui.theme.FitColors

@Composable
fun WeatherIllustration(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(width = 160.dp, height = 110.dp)) {
        val scaleX = size.width / 160f
        val scaleY = size.height / 110f

        // 1. Sun Glow: cx=100, cy=18, r=30 with blur
        val sunCenter = Offset(100f * scaleX, 18f * scaleY)
        val sunRadius = 30f * scaleX
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(FitColors.SunGlow.copy(alpha = 0.55f), Color.Transparent),
                center = sunCenter,
                radius = sunRadius * 1.5f
            ),
            radius = sunRadius * 1.5f,
            center = sunCenter
        )

        // 2. Sun Body: linear gradient #FFD93B -> #FF7416
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(FitColors.SunStart, FitColors.SunEnd),
                startY = sunCenter.y - sunRadius,
                endY = sunCenter.y + sunRadius
            ),
            radius = sunRadius,
            center = sunCenter
        )

        // 3. Cloud Path:
        // M33 100 A25 25 0 0 1 33 50 A24 24 0 0 1 78 36 A22 22 0 0 1 118 52 A25 25 0 0 1 124 100 Z
        val cloudPath = Path().apply {
            moveTo(33f * scaleX, 100f * scaleY)
            arcTo(
                rect = Rect(8f * scaleX, 50f * scaleY, 58f * scaleX, 100f * scaleY),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            arcTo(
                rect = Rect(30f * scaleX, 26f * scaleY, 78f * scaleX, 74f * scaleY),
                startAngleDegrees = 170f,
                sweepAngleDegrees = 150f,
                forceMoveTo = false
            )
            arcTo(
                rect = Rect(74f * scaleX, 30f * scaleY, 118f * scaleX, 74f * scaleY),
                startAngleDegrees = 200f,
                sweepAngleDegrees = 140f,
                forceMoveTo = false
            )
            arcTo(
                rect = Rect(74f * scaleX, 50f * scaleY, 124f * scaleX, 100f * scaleY),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            close()
        }

        // 4. Cloud Soft Shadow
        drawIntoCanvas { canvas ->
            val native = canvas.nativeCanvas
            val cloudShadowPaint = Paint().apply {
                isAntiAlias = true
                color = Color.Transparent.toArgb()
                setShadowLayer(6f * scaleX, 0f, 7f * scaleY, FitColors.CloudShadow.toArgb())
            }
            native.drawPath(cloudPath.asAndroidPath(), cloudShadowPaint)
        }

        // 5. Cloud Fill: linear gradient #FFFFFF -> #ECE6EF
        drawPath(
            path = cloudPath,
            brush = Brush.verticalGradient(
                colors = listOf(FitColors.CloudStart, FitColors.CloudEnd),
                startY = 20f * scaleY,
                endY = 100f * scaleY
            )
        )

        // 6. Orange Sunset Tint clipped to the cloud
        clipPath(cloudPath) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(FitColors.CloudTint, Color.Transparent),
                    center = Offset(100f * scaleX, 52f * scaleY),
                    radius = 46f * scaleX
                ),
                radius = 46f * scaleX,
                center = Offset(100f * scaleX, 52f * scaleY)
            )
        }
    }
}
