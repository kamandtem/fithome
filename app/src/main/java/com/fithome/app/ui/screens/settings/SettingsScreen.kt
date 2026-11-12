package com.fithome.app.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fithome.app.ui.components.DashboardDock
import com.fithome.app.ui.components.DetailHeader
import com.fithome.app.ui.components.FitShapes
import com.fithome.app.ui.components.NeumorphicToggle
import com.fithome.app.ui.modifiers.neumorphicButton
import com.fithome.app.ui.modifiers.neumorphicRaised
import com.fithome.app.ui.theme.FitColors
import com.fithome.app.ui.theme.Poppins

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onQuickStartClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var showWeightDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .verticalScroll(scrollState)
                .padding(bottom = 90.dp)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            DetailHeader(
                title = "Settings",
                hasUnreadNotifications = false,
                onBackClick = onBackClick,
                onNotificationClick = onNotificationClick
            )

            // Section: Preferences
            Text(
                text = "Preferences",
                fontFamily = Poppins,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = FitColors.TextPrimary,
                modifier = Modifier.padding(top = 10.dp)
            )

            // Metric / Imperial Units Setting
            SettingsCard(
                icon = Icons.Default.Straighten,
                iconColor = FitColors.BlueTabEnd,
                title = "Units of Measurement",
                subtitle = if (uiState.isMetric) "Metric (kg, cm, ml)" else "Imperial (lbs, in, oz)",
                actionContent = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEEF1F7))
                            .clickable { viewModel.toggleMetricUnits() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (uiState.isMetric) "Metric" else "Imperial",
                            fontFamily = Poppins,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FitColors.BlueTabEnd
                        )
                    }
                }
            )

            // Daily Reminders Toggle
            SettingsCard(
                icon = Icons.Default.Notifications,
                iconColor = FitColors.Gold,
                title = "Daily Workout Reminders",
                subtitle = if (uiState.remindersEnabled) "Enabled at 8:00 AM" else "Disabled",
                actionContent = {
                    NeumorphicToggle(
                        checked = uiState.remindersEnabled,
                        onCheckedChange = { viewModel.toggleReminders() },
                        contentDescriptionText = "Daily reminders toggle"
                    )
                }
            )

            // Daily Hydration Goal
            SettingsCard(
                icon = Icons.Default.WaterDrop,
                iconColor = FitColors.WaterDropEnd,
                title = "Hydration Daily Goal",
                subtitle = "${uiState.hydrationGoalMl} ml per day",
                actionContent = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEEF1F7))
                            .clickable {
                                val next = if (uiState.hydrationGoalMl >= 3000) 2000 else uiState.hydrationGoalMl + 500
                                viewModel.setHydrationGoal(next)
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Change",
                            fontFamily = Poppins,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = FitColors.TextPrimary
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Data Management Section
            Text(
                text = "Data Management",
                fontFamily = Poppins,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = FitColors.TextPrimary
            )

            // Clear All Data Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FitShapes.CardShape)
                    .background(Color.White)
                    .neumorphicRaised(shape = FitShapes.CardShape, darkBlur = 16.dp)
                    .clickable { viewModel.showClearDialog() }
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFEEEE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = null,
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Clear All Data",
                            fontFamily = Poppins,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE53935)
                        )
                        Text(
                            text = "Reset all workouts, sessions & preferences",
                            fontFamily = Poppins,
                            fontSize = 13.sp,
                            color = FitColors.TextMuted
                        )
                    }
                }
            }
        }

        // Fixed Dock
        DashboardDock(
            onHistoryClick = onHistoryClick,
            onQuickStartClick = onQuickStartClick,
            onSettingsClick = {},
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Clear Data Alert Dialog
        if (uiState.showClearDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissClearDialog() },
                title = {
                    Text(
                        text = "Reset Application Data?",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "This will permanently remove all workout logs, history, and user settings. Are you sure?",
                        fontFamily = Poppins
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.clearAllData() }
                    ) {
                        Text(
                            text = "Clear All",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE53935)
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissClearDialog() }) {
                        Text("Cancel", fontFamily = Poppins)
                    }
                }
            )
        }
    }
}

@Composable
private fun SettingsCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    actionContent: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FitShapes.CardShape)
            .background(Color.White)
            .neumorphicRaised(shape = FitShapes.CardShape, darkBlur = 14.dp)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3F5FA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = Poppins,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FitColors.TextDark
                )
                Text(
                    text = subtitle,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = FitColors.TextMuted
                )
            }

            actionContent()
        }
    }
}
