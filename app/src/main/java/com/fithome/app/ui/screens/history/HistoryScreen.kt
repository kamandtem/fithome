package com.fithome.app.ui.screens.history

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fithome.app.ui.components.DashboardDock
import com.fithome.app.ui.components.DetailHeader
import com.fithome.app.ui.components.FitShapes
import com.fithome.app.ui.modifiers.neumorphicGlow
import com.fithome.app.ui.modifiers.neumorphicInset
import com.fithome.app.ui.modifiers.neumorphicRaised
import com.fithome.app.ui.theme.FitColors
import com.fithome.app.ui.theme.Poppins
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBackClick: () -> Unit,
    onQuickStartClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(bottom = 90.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Header
            item {
                DetailHeader(
                    title = "Activity & History",
                    hasUnreadNotifications = false,
                    onBackClick = onBackClick,
                    onNotificationClick = onNotificationClick
                )
            }

            // Summary Stats Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    HistoryStatPill(
                        title = "Burned",
                        value = "${uiState.totalKcal}",
                        unit = "kcal",
                        modifier = Modifier.weight(1f)
                    )
                    HistoryStatPill(
                        title = "Time",
                        value = "${uiState.totalMinutes}",
                        unit = "min",
                        modifier = Modifier.weight(1f)
                    )
                    HistoryStatPill(
                        title = "Sessions",
                        value = "${uiState.sessions.size.coerceAtLeast(2)}",
                        unit = "done",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Weekly Neumorphic Bar Chart
            item {
                WeeklyBarChartCard(weeklyData = uiState.weeklyData)
            }

            // Completed Sessions Section Title
            item {
                Text(
                    text = "Recent Sessions",
                    fontFamily = Poppins,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FitColors.TextPrimary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Session List Items
            items(uiState.sessions) { session ->
                SessionHistoryItem(session = session)
            }
        }

        // Fixed Dock
        DashboardDock(
            onHistoryClick = {},
            onQuickStartClick = onQuickStartClick,
            onSettingsClick = onSettingsClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HistoryStatPill(
    title: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .neumorphicRaised(shape = RoundedCornerShape(22.dp), darkBlur = 16.dp)
            .padding(vertical = 14.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    fontFamily = Poppins,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = FitColors.TextDark
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = unit,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = FitColors.TextMuted,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Text(
                text = title,
                fontFamily = Poppins,
                fontSize = 12.sp,
                color = FitColors.TextSecondary
            )
        }
    }
}

@Composable
private fun WeeklyBarChartCard(
    weeklyData: List<DayKcal>
) {
    val maxKcal = (weeklyData.maxOfOrNull { it.kcal } ?: 800).toFloat()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White)
            .neumorphicRaised(shape = RoundedCornerShape(28.dp), darkBlur = 20.dp)
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Weekly Activity",
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FitColors.TextPrimary
                )
                Text(
                    text = "Daily Avg: 520 kcal",
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = FitColors.TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bars Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                weeklyData.forEach { item ->
                    val fraction = (item.kcal / maxKcal).coerceIn(0.15f, 1.0f)
                    val barHeight = (100 * fraction).dp

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        // The Neumorphic Bar
                        Box(
                            modifier = Modifier
                                .size(width = 18.dp, height = 100.dp)
                                .clip(RoundedCornerShape(9.dp))
                                .background(Color(0xFFEEF1F7))
                                .neumorphicInset(shape = RoundedCornerShape(9.dp)),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 18.dp, height = barHeight)
                                    .clip(RoundedCornerShape(9.dp))
                                    .then(
                                        if (item.isToday) {
                                            Modifier.neumorphicGlow(FitColors.BlueTabGlow, radius = 6.dp)
                                        } else Modifier
                                    )
                                    .background(
                                        if (item.isToday) {
                                            Brush.verticalGradient(
                                                listOf(FitColors.BlueTabStart, FitColors.BlueTabEnd)
                                            )
                                        } else {
                                            Brush.verticalGradient(
                                                listOf(FitColors.PurpleCardStart, FitColors.PurpleCardEnd)
                                            )
                                        }
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = item.dayName,
                            fontFamily = Poppins,
                            fontSize = 12.sp,
                            fontWeight = if (item.isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (item.isToday) FitColors.BlueTabEnd else FitColors.TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionHistoryItem(session: com.fithome.app.data.model.Session) {
    val dateStr = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
        .format(Date(session.startedAt))

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
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = FitColors.PurpleCardEnd,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.workoutName,
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FitColors.TextDark
                )
                Text(
                    text = dateStr,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = FitColors.TextMuted
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${session.kcal} kcal",
                    fontFamily = Poppins,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FitColors.TextDark
                )
                Text(
                    text = "${session.durationSec / 60}m @ Lvl ${session.intensity}",
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = FitColors.TextSecondary
                )
            }
        }
    }
}
