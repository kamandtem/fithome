package com.fithome.app.ui.modifiers

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.fithome.app.ui.theme.FitColors

/**
 * Neumorphic raised surface with dual soft shadows (dark bottom-right, white top-left).
 */
fun Modifier.neumorphicRaised(
    shape: Shape = RoundedCornerShape(26.dp),
    darkShadowColor: Color = FitColors.ShadowDark,
    lightShadowColor: Color = FitColors.ShadowLight,
    darkOffset: DpOffset = DpOffset(0.dp, 10.dp),
    lightOffset: DpOffset = DpOffset((-4).dp, (-4).dp),
    darkBlur: Dp = 14.dp,
    lightBlur: Dp = 8.dp
): Modifier = this.drawBehind {
    val darkDx = darkOffset.x.toPx()
    val darkDy = darkOffset.y.toPx()
    val darkBlurPx = darkBlur.toPx().coerceAtLeast(1f)

    val lightDx = lightOffset.x.toPx()
    val lightDy = lightOffset.y.toPx()
    val lightBlurPx = lightBlur.toPx().coerceAtLeast(1f)

    val outline = shape.createOutline(size, layoutDirection, this)
    val path = Path().apply {
        when (outline) {
            is Outline.Rectangle -> addRect(outline.rect)
            is Outline.Rounded -> addRoundRect(outline.roundRect)
            is Outline.Generic -> addPath(outline.path)
        }
    }
    val androidPath = path.asAndroidPath()

    drawIntoCanvas { canvas ->
        val nativeCanvas = canvas.nativeCanvas

        // 1. Draw dark shadow
        val darkPaint = Paint().apply {
            isAntiAlias = true
            color = darkShadowColor.toArgb()
            setShadowLayer(darkBlurPx, darkDx, darkDy, darkShadowColor.toArgb())
        }
        nativeCanvas.drawPath(androidPath, darkPaint)

        // 2. Draw light shadow
        val lightPaint = Paint().apply {
            isAntiAlias = true
            color = lightShadowColor.toArgb()
            setShadowLayer(lightBlurPx, lightDx, lightDy, lightShadowColor.toArgb())
        }
        nativeCanvas.drawPath(androidPath, lightPaint)
    }
}

/**
 * Neumorphic inset surface (pressed well / groove).
 */
fun Modifier.neumorphicInset(
    shape: Shape = RoundedCornerShape(26.dp),
    topShadowColor: Color = Color(0x4D96A5C3),
    bottomShadowColor: Color = Color(0xCCFFFFFF),
    depth: Dp = 6.dp
): Modifier = this
    .clip(shape)
    .drawWithContent {
        drawContent()
        val depthPx = depth.toPx()

        // Top-left dark inner shadow gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(topShadowColor, Color.Transparent),
                startY = 0f,
                endY = depthPx
            ),
            size = Size(size.width, depthPx)
        )

        // Bottom-right light inner shadow gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, bottomShadowColor),
                startY = size.height - depthPx,
                endY = size.height
            ),
            topLeft = Offset(0f, size.height - depthPx),
            size = Size(size.width, depthPx)
        )
    }

/**
 * Neumorphic button interaction with 0.97 scale animation and haptic feedback.
 */
fun Modifier.neumorphicButton(
    shape: Shape = CircleShape,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = tween(durationMillis = 100),
        label = "button_scale"
    )

    this
        .scale(scale)
        .then(
            if (isPressed) {
                Modifier.neumorphicInset(shape = shape)
            } else {
                Modifier.neumorphicRaised(
                    shape = shape,
                    darkOffset = DpOffset(0.dp, 8.dp),
                    darkBlur = 16.dp,
                    lightOffset = DpOffset((-3).dp, (-3).dp),
                    lightBlur = 8.dp
                )
            }
        )
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onClick()
        }
}

/**
 * Radial glowing effect behind controls.
 */
fun Modifier.neumorphicGlow(
    glowColor: Color,
    radius: Dp = 16.dp
): Modifier = this.drawBehind {
    val radiusPx = radius.toPx()
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(glowColor, Color.Transparent),
            center = center,
            radius = size.minDimension / 2f + radiusPx
        ),
        radius = size.minDimension / 2f + radiusPx,
        center = center
    )
}
