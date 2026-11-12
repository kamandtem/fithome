package com.fithome.app.ui.components

import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fithome.app.ui.theme.FitColors
import com.fithome.app.ui.theme.Poppins
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun IntensityDial(
    intensity: Int,
    onIntensityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var lastHapticValue by remember { mutableIntStateOf(intensity) }
    var currentAngle by remember { mutableFloatStateOf(0f) }

    // Animated intensity for fluid arc motion
    val animatedIntensity by animateFloatAsState(
        targetValue = intensity.toFloat(),
        animationSpec = tween(durationMillis = 150),
        label = "animated_intensity"
    )

    // Warm arc: starting at 270 deg (top), clockwise sweep = (v - 10) / 20 * 146 deg
    val warmSweep = ((animatedIntensity - 10f) / 20f * 146f).coerceIn(0f, 146f)

    // Cool arc: starting at 90 deg (bottom), clockwise sweep = (30 - v) / 20 * 212 deg
    val coolSweep = ((30f - animatedIntensity) / 20f * 212f).coerceIn(0f, 212f)

    Box(
        modifier = modifier
            .size(360.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val dx = offset.x - size.width / 2f
                        val dy = offset.y - size.height / 2f
                        currentAngle = atan2(dy, dx) * 180f / PI.toFloat()
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        val dx = change.position.x - size.width / 2f
                        val dy = change.position.y - size.height / 2f
                        val newAngle = atan2(dy, dx) * 180f / PI.toFloat()

                        var angleDiff = newAngle - currentAngle
                        if (angleDiff > 180f) angleDiff -= 360f
                        if (angleDiff < -180f) angleDiff += 360f

                        val delta = (angleDiff / 10f)
                        if (kotlin.math.abs(delta) >= 0.5f) {
                            val newIntensity = (intensity + delta.roundToInt()).coerceIn(10, 30)
                            if (newIntensity != intensity) {
                                onIntensityChange(newIntensity)
                                if (newIntensity != lastHapticValue) {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    lastHapticValue = newIntensity
                                }
                            }
                            currentAngle = newAngle
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val scale = size.width / 360f

            // 1. Ticks: 19 ticks mirrored (-171° to -9°, step 9°)
            val tickPaint = Paint().apply {
                isAntiAlias = true
                color = FitColors.DialTicks.toArgb()
                strokeWidth = 2f * scale
                strokeCap = Paint.Cap.ROUND
            }
            drawIntoCanvas { canvas ->
                val native = canvas.nativeCanvas
                for (i in -9..9) {
                    val angleDeg = -90f + i * 9f
                    val rad = angleDeg * PI.toFloat() / 180f
                    val rInner = 143f * scale
                    val rOuter = 153f * scale

                    val x1 = cx + rInner * cos(rad)
                    val y1 = cy + rInner * sin(rad)
                    val x2 = cx + rOuter * cos(rad)
                    val y2 = cy + rOuter * sin(rad)
                    native.drawLine(x1, y1, x2, y2, tickPaint)
                }
            }

            // 2. Outer Ring: r=132 with drop shadow
            val rRing = 132f * scale
            drawIntoCanvas { canvas ->
                val native = canvas.nativeCanvas
                val ringShadowPaint = Paint().apply {
                    isAntiAlias = true
                    color = Color.Transparent.toArgb()
                    setShadowLayer(9f * scale, 0f, 10f * scale, Color(0x808D9CB8).toArgb())
                }
                native.drawCircle(cx, cy, rRing, ringShadowPaint)
            }
            drawCircle(
                brush = Brush.linearGradient(
                    colors = listOf(FitColors.DialRingStart, FitColors.DialRingEnd),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                ),
                radius = rRing,
                center = Offset(cx, cy)
            )

            // 3. Track: r=119
            val rTrack = 119f * scale
            drawCircle(
                brush = Brush.linearGradient(
                    colors = listOf(FitColors.DialTrackStart, FitColors.DialTrackEnd),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                ),
                radius = rTrack,
                center = Offset(cx, cy)
            )
            drawCircle(
                color = Color(0x59FFFFFF),
                radius = rTrack,
                center = Offset(cx, cy),
                style = Stroke(width = 1.5f * scale)
            )

            // 4. Arcs: width 12 at r=105
            val rArc = 105f * scale
            val arcRect = Size(rArc * 2f, rArc * 2f)
            val arcTopLeft = Offset(cx - rArc, cy - rArc)

            // Cool Arc (starts at 90°, runs clockwise)
            drawArc(
                brush = Brush.linearGradient(
                    colors = listOf(FitColors.CyanArcStart, FitColors.CyanArcEnd),
                    start = Offset(cx, cy + rArc),
                    end = Offset(cx - rArc, cy)
                ),
                startAngle = 90f,
                sweepAngle = coolSweep,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcRect,
                style = Stroke(width = 12f * scale, cap = StrokeCap.Round)
            )

            // Warm Arc (starts at 270°, runs clockwise)
            drawArc(
                brush = Brush.linearGradient(
                    colors = listOf(FitColors.MagentaArcStart, FitColors.MagentaArcEnd),
                    start = Offset(cx, cy - rArc),
                    end = Offset(cx + rArc, cy)
                ),
                startAngle = 270f,
                sweepAngle = warmSweep,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcRect,
                style = Stroke(width = 12f * scale, cap = StrokeCap.Round)
            )

            // 5. Inner Disc: r=90 with drop shadow
            val rInner = 90f * scale
            drawIntoCanvas { canvas ->
                val native = canvas.nativeCanvas
                val innerShadowPaint = Paint().apply {
                    isAntiAlias = true
                    color = Color.Transparent.toArgb()
                    setShadowLayer(6f * scale, 0f, 5f * scale, Color(0x738795B0).toArgb())
                }
                native.drawCircle(cx, cy, rInner, innerShadowPaint)
            }
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(FitColors.DialInnerStart, FitColors.DialInnerEnd),
                    center = Offset(cx - 15f * scale, cy - 25f * scale),
                    radius = rInner * 1.5f
                ),
                radius = rInner,
                center = Offset(cx, cy)
            )

            // 6. Two knobs:
            // Top knob: at (cx, cy - rArc) r=14
            val topKnobCenter = Offset(cx, cy - rArc)
            drawIntoCanvas { canvas ->
                val native = canvas.nativeCanvas
                val knobShadow = Paint().apply {
                    isAntiAlias = true
                    color = Color.Transparent.toArgb()
                    setShadowLayer(4f * scale, 0f, 2f * scale, Color(0x806F7F9D).toArgb())
                }
                native.drawCircle(topKnobCenter.x, topKnobCenter.y, 14f * scale, knobShadow)
            }
            drawCircle(
                color = Color.White,
                radius = 14f * scale,
                center = topKnobCenter
            )
            // Top knob star icon
            drawCircle(
                color = Color(0xFF9AA5BA),
                radius = 2.5f * scale,
                center = topKnobCenter
            )
            val iconPaint = Paint().apply {
                isAntiAlias = true
                color = 0xFF9AA5BA.toInt()
                strokeWidth = 1.4f * scale
                strokeCap = Paint.Cap.ROUND
                style = Paint.Style.STROKE
            }
            drawIntoCanvas { canvas ->
                val native = canvas.nativeCanvas
                native.drawLine(topKnobCenter.x, topKnobCenter.y - 7f * scale, topKnobCenter.x, topKnobCenter.y - 5f * scale, iconPaint)
                native.drawLine(topKnobCenter.x, topKnobCenter.y + 5f * scale, topKnobCenter.x, topKnobCenter.y + 7f * scale, iconPaint)
                native.drawLine(topKnobCenter.x - 7f * scale, topKnobCenter.y, topKnobCenter.x - 5f * scale, topKnobCenter.y, iconPaint)
                native.drawLine(topKnobCenter.x + 5f * scale, topKnobCenter.y, topKnobCenter.x + 7f * scale, topKnobCenter.y, iconPaint)
            }

            // Bottom knob: at (cx, cy + rArc) r=16
            val bottomKnobCenter = Offset(cx, cy + rArc)
            drawIntoCanvas { canvas ->
                val native = canvas.nativeCanvas
                val knobShadow = Paint().apply {
                    isAntiAlias = true
                    color = Color.Transparent.toArgb()
                    setShadowLayer(4f * scale, 0f, 2f * scale, Color(0x806F7F9D).toArgb())
                }
                native.drawCircle(bottomKnobCenter.x, bottomKnobCenter.y, 16f * scale, knobShadow)
            }
            drawCircle(
                color = Color.White,
                radius = 16f * scale,
                center = bottomKnobCenter
            )
            // Bottom knob propeller/flower icon
            drawCircle(
                color = Color(0xFF9AA5BA),
                radius = 2.2f * scale,
                center = bottomKnobCenter
            )
        }

        // Top mini value indicator
        Text(
            text = "$intensity",
            fontFamily = Poppins,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF343B4F),
            modifier = Modifier.offset(y = (-162).dp)
        )

        // Left decrement label "10"
        Box(
            modifier = Modifier
                .offset(x = (-158).dp, y = 10.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (intensity > 10) {
                        onIntensityChange(intensity - 1)
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                }
        ) {
            Text(
                text = "10",
                fontFamily = Poppins,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF343B4F)
            )
        }

        // Right increment label "30"
        Box(
            modifier = Modifier
                .offset(x = 158.dp, y = 10.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (intensity < 30) {
                        onIntensityChange(intensity + 1)
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                }
        ) {
            Text(
                text = "30",
                fontFamily = Poppins,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF343B4F)
            )
        }

        // Center primary intensity display
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-2).dp)
        ) {
            Text(
                text = "$intensity",
                fontFamily = Poppins,
                fontSize = 64.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-1).sp,
                color = FitColors.TextDark
            )
            Text(
                text = "Intensity",
                fontFamily = Poppins,
                fontSize = 16.sp,
                color = FitColors.TextSecondary,
                modifier = Modifier.offset(y = (-4).dp)
            )
        }
    }
}
