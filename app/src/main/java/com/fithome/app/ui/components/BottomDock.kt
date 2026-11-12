package com.fithome.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fithome.app.ui.modifiers.neumorphicButton
import com.fithome.app.ui.modifiers.neumorphicGlow
import com.fithome.app.ui.modifiers.neumorphicInset
import com.fithome.app.ui.modifiers.neumorphicRaised
import com.fithome.app.ui.theme.FitColors

/**
 * The fixed bottom dock for Dashboard and extra screens.
 * Features:
 * - Left button: History & Stats screen
 * - Center button: Quick Start workout
 * - Right button: Settings screen
 */
@Composable
fun DashboardDock(
    onHistoryClick: () -> Unit,
    onQuickStartClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(86.dp)
    ) {
        // Curved dock bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .align(Alignment.BottomCenter)
                .clip(FitShapes.DashboardDock)
                .background(
                    Brush.horizontalGradient(
                        0.0f to FitColors.DockStart,
                        0.38f to FitColors.DockMid,
                        1.0f to FitColors.DockEnd
                    )
                )
                .neumorphicInset(
                    shape = FitShapes.DashboardDock,
                    topShadowColor = Color.White.copy(alpha = 0.9f),
                    bottomShadowColor = Color.Transparent,
                    depth = 3.dp
                )
        )

        // Left button: History / Stats
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 55.dp, y = (-14).dp)
                .size(50.dp)
                .neumorphicButton(shape = CircleShape, onClick = onHistoryClick)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(Color.White, Color(0xFFF0F3F9)))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.BarChart,
                contentDescription = "History and Stats",
                tint = Color(0xFF9FA8BB),
                modifier = Modifier.size(24.dp)
            )
        }

        // Center button: Quick Start (64x64)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-14).dp)
                .size(64.dp)
                .neumorphicRaised(
                    shape = CircleShape,
                    darkShadowColor = FitColors.ShadowDark,
                    lightShadowColor = Color.White,
                    darkBlur = 20.dp
                )
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color.White, Color(0xFFF6F8FC))
                    )
                )
                .neumorphicButton(shape = CircleShape, onClick = onQuickStartClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PowerSettingsNew,
                contentDescription = "Quick Start Workout",
                tint = FitColors.Gold,
                modifier = Modifier.size(28.dp)
            )
        }

        // Right button: Settings
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-55).dp, y = (-14).dp)
                .size(50.dp)
                .neumorphicGlow(Color(0x4D3CBEFF), radius = 10.dp)
                .neumorphicButton(shape = CircleShape, onClick = onSettingsClick)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(Color.White, Color(0xFFF0F3F9)))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = FitColors.WaterDropEnd,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Screen 1 Session Dock:
 * Includes side pill wells, Hydration action, and Tips button.
 */
@Composable
fun SessionDock(
    onHydrationClick: () -> Unit,
    onTipsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(86.dp)
    ) {
        // Left side pill well
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(width = 150.dp, height = 80.dp)
                .clip(FitShapes.DockPill)
                .background(
                    Brush.verticalGradient(listOf(FitColors.DockWellStart, FitColors.DockWellEnd))
                )
                .neumorphicInset(
                    shape = FitShapes.DockPill,
                    topShadowColor = Color(0x4D96A5C3),
                    bottomShadowColor = Color.White.copy(alpha = 0.8f)
                )
        )

        // Right side pill well
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(width = 150.dp, height = 80.dp)
                .clip(FitShapes.DockPill)
                .background(
                    Brush.verticalGradient(listOf(FitColors.DockWellStart, FitColors.DockWellEnd))
                )
                .neumorphicInset(
                    shape = FitShapes.DockPill,
                    topShadowColor = Color(0x4D96A5C3),
                    bottomShadowColor = Color.White.copy(alpha = 0.8f)
                )
        )

        // Hydration button: left side (at 57dp, 812dp in HTML)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 51.dp, y = (-16).dp)
                .size(48.dp)
                .neumorphicButton(shape = CircleShape, onClick = onHydrationClick)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(Color.White, Color(0xFFF0F3F9)))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.WaterDrop,
                contentDescription = "Log 250ml Water",
                tint = FitColors.WaterDropEnd,
                modifier = Modifier.size(24.dp)
            )
        }

        // Tips button: right side (at 298dp, 812dp in HTML)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-57).dp, y = (-16).dp)
                .size(48.dp)
                .neumorphicButton(shape = CircleShape, onClick = onTipsClick)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(Color.White, Color(0xFFF0F3F9)))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = "Workout Tips",
                tint = Color(0xFFC9CFDB),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
