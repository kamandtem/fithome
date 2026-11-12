package com.fithome.app.ui.screens.session

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fithome.app.ui.components.DetailHeader
import com.fithome.app.ui.components.FitShapes
import com.fithome.app.ui.components.IntensityDial
import com.fithome.app.ui.components.SessionDock
import com.fithome.app.ui.components.StepperDialog
import com.fithome.app.ui.components.TShapeSurface
import com.fithome.app.ui.components.WorkoutSummarySheet
import com.fithome.app.ui.modifiers.neumorphicButton
import com.fithome.app.ui.modifiers.neumorphicGlow
import com.fithome.app.ui.modifiers.neumorphicInset
import com.fithome.app.ui.modifiers.neumorphicRaised
import com.fithome.app.ui.theme.FitColors
import com.fithome.app.ui.theme.Poppins

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSessionScreen(
    viewModel: WorkoutSessionViewModel,
    onBackClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {

        // Scrollable content area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .verticalScroll(scrollState)
        ) {
            // Container with 403 x 876 design height reference
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(876.dp)
            ) {

                // 1. Raised T-shaped architectural surface (panel + stem)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .neumorphicRaised(
                            shape = TShapeSurface(startTopY = 250f, neckY = 690f),
                            darkShadowColor = FitColors.ShadowDark,
                            lightShadowColor = FitColors.ShadowLight,
                            darkBlur = 14.dp,
                            lightBlur = 8.dp
                        )
                        .clip(TShapeSurface(startTopY = 250f, neckY = 690f))
                        .background(
                            Brush.verticalGradient(
                                listOf(FitColors.TShapeTop, FitColors.TShapeBottom)
                            )
                        )
                )

                // 2. Header
                DetailHeader(
                    title = "${uiState.selectedCategory} Session",
                    hasUnreadNotifications = uiState.hasUnreadNotifications,
                    onBackClick = onBackClick,
                    onNotificationClick = onNotificationClick,
                    modifier = Modifier.offset(y = 20.dp)
                )

                // 3. Category Tabs (Cardio, Strength, Yoga)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 96.dp)
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    uiState.tabs.forEach { tab ->
                        val isSelected = tab.category == uiState.selectedCategory
                        SessionTabItem(
                            title = tab.title,
                            subtitle = tab.subtitle,
                            isSelected = isSelected,
                            category = tab.category,
                            onClick = { viewModel.selectTab(tab.category) }
                        )
                    }
                }

                // 4. Custom Intensity Dial
                IntensityDial(
                    intensity = uiState.currentIntensity,
                    onIntensityChange = { viewModel.setIntensity(it) },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = 246.dp)
                )

                // 5. Auto / Manual Intensity Pill
                val isAuto = uiState.isAutoIntensity
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = 608.dp)
                        .size(width = 72.dp, height = 30.dp)
                        .clip(FitShapes.PillShape)
                        .background(
                            if (isAuto) {
                                Brush.linearGradient(listOf(FitColors.BlueTabStart, FitColors.BlueTabEnd))
                            } else {
                                Brush.linearGradient(listOf(Color(0xFFDDE3EE), Color(0xFFDDE3EE)))
                            }
                        )
                        .then(
                            if (!isAuto) {
                                Modifier.neumorphicInset(
                                    shape = FitShapes.PillShape,
                                    topShadowColor = Color(0x668C9EB8),
                                    bottomShadowColor = Color.White
                                )
                            } else {
                                Modifier.neumorphicGlow(FitColors.BlueTabGlow, radius = 6.dp)
                            }
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            viewModel.toggleAutoIntensity()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isAuto) "Auto" else "Manual",
                        fontFamily = Poppins,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isAuto) Color.White else Color(0xFF7A8499)
                    )
                }

                // 6. Stats Row: Kcal (left) & Duration (right)
                // Left Stat: Estimated Kcal
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 34.dp, y = 660.dp)
                ) {
                    Text(
                        text = "${uiState.estimatedKcal} kcal",
                        fontFamily = Poppins,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = FitColors.TextDark
                    )
                    Text(
                        text = "Estimated",
                        fontFamily = Poppins,
                        fontSize = 15.sp,
                        color = FitColors.TextMuted
                    )
                }

                // Right Stat: Duration (tap to open Stepper Dialog)
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-34).dp, y = 660.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            viewModel.openStepperDialog()
                        }
                ) {
                    Text(
                        text = if (uiState.timerState == TimerState.RUNNING) {
                            val m = uiState.remainingSeconds / 60
                            val s = uiState.remainingSeconds % 60
                            String.format("%02d:%02d", m, s)
                        } else {
                            "${uiState.currentDurationMinutes}m"
                        },
                        fontFamily = Poppins,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = FitColors.TextDark
                    )
                    Text(
                        text = if (uiState.timerState == TimerState.RUNNING) "Remaining" else "Duration",
                        fontFamily = Poppins,
                        fontSize = 15.sp,
                        color = FitColors.TextMuted
                    )
                }

                // 7. Big Start / Pause / Resume Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = 736.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .neumorphicRaised(
                                shape = CircleShape,
                                darkShadowColor = FitColors.ShadowDark,
                                lightShadowColor = Color.White,
                                darkBlur = 24.dp
                            )
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color.White, Color(0xFFF6F8FC))
                                )
                            )
                            .combinedClickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { viewModel.togglePlayPause() },
                                onLongClick = {
                                    if (uiState.timerState != TimerState.IDLE) {
                                        viewModel.finishWorkout()
                                    }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        FingerprintWaveIcon(
                            color = FitColors.Gold,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = when (uiState.timerState) {
                            TimerState.IDLE -> "Start Workout"
                            TimerState.RUNNING -> "Pause (Hold to Finish)"
                            TimerState.PAUSED -> "Resume"
                        },
                        fontFamily = Poppins,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFC6CCD9)
                    )
                }
            }
        }

        // 8. Fixed Bottom Dock (Hydration & Tips)
        SessionDock(
            onHydrationClick = { viewModel.logHydration() },
            onTipsClick = { viewModel.openTipsSheet() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // 9. Hydration Animated Toast
        AnimatedVisibility(
            visible = uiState.hydrationToastVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 40 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { 40 }),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 36.dp, y = (-96).dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF2C8EF5))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "+250 ml logged!",
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        // Stepper Dialog
        if (uiState.showStepperDialog) {
            StepperDialog(
                initialMinutes = uiState.currentDurationMinutes,
                onConfirm = { viewModel.setDuration(it) },
                onDismiss = { viewModel.dismissStepperDialog() }
            )
        }

        // Workout Summary Sheet
        if (uiState.showSummarySheet && uiState.finishedSession != null) {
            val session = uiState.finishedSession!!
            WorkoutSummarySheet(
                workoutName = session.workoutName,
                durationSec = session.durationSec,
                kcal = session.kcal,
                intensity = session.intensity,
                onDismiss = { viewModel.dismissSummarySheet() }
            )
        }

        // Workout Tips Sheet
        if (uiState.showTipsSheet) {
            WorkoutTipsBottomSheet(
                category = uiState.selectedCategory,
                onDismiss = { viewModel.dismissTipsSheet() }
            )
        }
    }
}

@Composable
private fun SessionTabItem(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    category: String,
    onClick: () -> Unit
) {
    val tabShape = FitShapes.TabShape
    val icon: ImageVector = when (category) {
        "Cardio" -> Icons.Default.DirectionsRun
        "Strength" -> Icons.Default.FitnessCenter
        else -> Icons.Default.SelfImprovement
    }

    Box(
        modifier = Modifier
            .size(width = 104.dp, height = 120.dp)
            .then(
                if (isSelected) {
                    Modifier.neumorphicRaised(
                        shape = tabShape,
                        darkShadowColor = FitColors.BlueTabGlow,
                        lightShadowColor = Color.Transparent,
                        darkBlur = 24.dp
                    )
                } else {
                    Modifier.neumorphicRaised(
                        shape = tabShape,
                        darkShadowColor = Color(0x66A0AFCD),
                        lightShadowColor = Color.White,
                        darkBlur = 20.dp
                    )
                }
            )
            .clip(tabShape)
            .background(
                if (isSelected) {
                    Brush.linearGradient(
                        colors = listOf(FitColors.BlueTabStart, FitColors.BlueTabEnd)
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(Color.White, Color(0xFFF8FAFF))
                    )
                }
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(top = 15.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Icon Circle
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) Color.White.copy(alpha = 0.14f) else Color.White
                    )
                    .border(
                        width = 2.dp,
                        color = if (isSelected) Color.White.copy(alpha = 0.75f) else when (category) {
                            "Strength" -> FitColors.GoldLight
                            "Yoga" -> FitColors.Magenta
                            else -> Color(0xFFDFE4EE)
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) Color.White else when (category) {
                        "Strength" -> FitColors.GoldStroke
                        "Yoga" -> FitColors.Magenta
                        else -> Color(0xFF3B4357)
                    },
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontFamily = Poppins,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Color.White else Color(0xFF262D41)
            )

            Text(
                text = subtitle,
                fontFamily = Poppins,
                fontSize = 13.sp,
                color = if (isSelected) Color.White.copy(alpha = 0.62f) else Color(0xFFB9C0CD)
            )
        }
    }
}

/**
 * Fingerprint Wave Vector ported 1:1 from the HTML SVG:
 * d="M8 22c0-8 6-14 14-14s14 6 14 14c0 6-1 11-4 15"
 * d="M13 24c0-5 4-9 9-9s9 4 9 9c0 5-1 9-3 13"
 * d="M18 25c0-2.5 2-4.5 4-4.5s4 2 4 4.5c0 5-1 9-3 13"
 * d="M22 26v6c0 3-.5 6-2 9"
 * d="M12 36c1-3 1.5-6 1.5-9"
 * d="M8 30c0 3-.5 5-1.5 7"
 */
@Composable
private fun FingerprintWaveIcon(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val sx = size.width / 44f
        val sy = size.height / 48f
        val stroke = Stroke(width = 2.2f * sx)

        val path1 = Path().apply {
            moveTo(8f * sx, 22f * sy)
            cubicTo(8f * sx, 14f * sy, 14f * sx, 8f * sy, 22f * sx, 8f * sy)
            cubicTo(30f * sx, 8f * sy, 36f * sx, 14f * sy, 36f * sx, 22f * sy)
            cubicTo(36f * sx, 28f * sy, 35f * sx, 33f * sy, 32f * sx, 37f * sy)
        }
        drawPath(path1, color, style = stroke)

        val path2 = Path().apply {
            moveTo(13f * sx, 24f * sy)
            cubicTo(13f * sx, 19f * sy, 17f * sx, 15f * sy, 22f * sx, 15f * sy)
            cubicTo(27f * sx, 15f * sy, 31f * sx, 19f * sy, 31f * sx, 24f * sy)
            cubicTo(31f * sx, 29f * sy, 30f * sx, 33f * sy, 28f * sx, 37f * sy)
        }
        drawPath(path2, color, style = stroke)

        val path3 = Path().apply {
            moveTo(18f * sx, 25f * sy)
            cubicTo(18f * sx, 22.5f * sy, 20f * sx, 20.5f * sy, 22f * sx, 20.5f * sy)
            cubicTo(24f * sx, 20.5f * sy, 26f * sx, 22.5f * sy, 26f * sx, 25f * sy)
            cubicTo(26f * sx, 30f * sy, 25f * sx, 34f * sy, 23f * sx, 38f * sy)
        }
        drawPath(path3, color, style = stroke)

        val path4 = Path().apply {
            moveTo(22f * sx, 26f * sy)
            lineTo(22f * sx, 32f * sy)
            cubicTo(22f * sx, 35f * sy, 21.5f * sx, 38f * sy, 20f * sx, 41f * sy)
        }
        drawPath(path4, color, style = stroke)

        val path5 = Path().apply {
            moveTo(12f * sx, 36f * sy)
            cubicTo(13f * sx, 33f * sy, 13.5f * sx, 30f * sy, 13.5f * sx, 27f * sy)
        }
        drawPath(path5, color, style = stroke)

        val path6 = Path().apply {
            moveTo(8f * sx, 30f * sy)
            cubicTo(8f * sx, 33f * sy, 7.5f * sx, 35f * sy, 6.5f * sx, 37f * sy)
        }
        drawPath(path6, color, style = stroke)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkoutTipsBottomSheet(
    category: String,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = FitColors.BgTop,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "$category Coach Tip",
                fontFamily = Poppins,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = FitColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = when (category) {
                    "Cardio" -> "Maintain an upright cadence and breathe rhythmically every 3 strides. If your heart rate spikes above 160 bpm, lower intensity by 2 levels."
                    "Strength" -> "Focus on time under tension. Aim for 3 seconds on the eccentric (lowering) phase and 1 second on the concentric drive."
                    else -> "Inhale as you expand the chest, exhale as you deepen into the posture. Keep your core gently engaged for pelvic stability."
                },
                fontFamily = Poppins,
                fontSize = 15.sp,
                color = FitColors.TextSecondary,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
