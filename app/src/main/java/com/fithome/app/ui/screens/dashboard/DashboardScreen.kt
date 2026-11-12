package com.fithome.app.ui.screens.dashboard

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fithome.app.ui.components.AvatarView
import com.fithome.app.ui.components.DashboardDock
import com.fithome.app.ui.components.DashboardHeader
import com.fithome.app.ui.components.FitShapes
import com.fithome.app.ui.components.WeatherIllustration
import com.fithome.app.ui.components.WorkoutCard
import com.fithome.app.ui.modifiers.neumorphicButton
import com.fithome.app.ui.modifiers.neumorphicGlow
import com.fithome.app.ui.modifiers.neumorphicInset
import com.fithome.app.ui.modifiers.neumorphicRaised
import com.fithome.app.ui.theme.FitColors
import com.fithome.app.ui.theme.Poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onWorkoutClick: (Long) -> Unit,
    onQuickStartClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Photo picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setAvatarUri(uri.toString())
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Scrolling grid covering everything and scrolling nicely under the fixed dock
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                top = 20.dp,
                bottom = 100.dp,
                start = 18.dp,
                end = 18.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // 1. Profile Header
            item(span = { GridItemSpan(2) }) {
                DashboardHeader(
                    userName = uiState.userName,
                    hasUnreadNotifications = uiState.hasUnreadNotifications,
                    onProfileClick = onProfileClick,
                    onAddPhotoClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onNotificationClick = onNotificationClick,
                    avatarContent = {
                        AvatarView(avatarUri = uiState.avatarUri)
                    }
                )
            }

            // 2. Weather Section
            item(span = { GridItemSpan(2) }) {
                WeatherCompositeCard(
                    isToday = uiState.isWeatherTodaySelected,
                    temp = uiState.weatherInfo?.temperature ?: 23,
                    lowTemp = uiState.weatherInfo?.lowTemperature ?: 13,
                    description = uiState.weatherInfo?.description ?: "A sun with a cool breeze",
                    sensible = uiState.weatherInfo?.sensibleTemperature ?: 25,
                    humidity = uiState.weatherInfo?.humidityPercent ?: 63,
                    pressure = uiState.weatherInfo?.pressureHpa ?: 1009,
                    onToggleTab = { viewModel.toggleWeatherTab() },
                    onClick = { viewModel.openWeatherDetails() }
                )
            }

            // 3. "My workouts" Title & Menu
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = buildAnnotatedString {
                                append("My ")
                                withStyle(SpanStyle(color = Color(0xFFE79A3B))) {
                                    append("workouts")
                                }
                            },
                            fontFamily = Poppins,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FitColors.TextPrimary
                        )
                    }

                    Box {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .neumorphicButton(shape = CircleShape) {
                                    viewModel.toggleMenuDropdown()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = "Workout menu",
                                tint = Color(0xFFB7BDCA)
                            )
                        }

                        DropdownMenu(
                            expanded = uiState.showMenuDropdown,
                            onDismissRequest = { viewModel.toggleMenuDropdown() }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Add Workout", fontFamily = Poppins) },
                                leadingIcon = { Icon(Icons.Default.Add, null) },
                                onClick = { viewModel.openAddWorkoutDialog() }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort by Name", fontFamily = Poppins) },
                                leadingIcon = { Icon(Icons.Default.SortByAlpha, null) },
                                onClick = { viewModel.sortWorkouts(byName = true) }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort by Time", fontFamily = Poppins) },
                                leadingIcon = { Icon(Icons.Default.Sort, null) },
                                onClick = { viewModel.sortWorkouts(byName = false) }
                            )
                        }
                    }
                }
            }

            // 4. Calories / Energy Row
            item(span = { GridItemSpan(2) }) {
                CaloriesEnergyPill(
                    kcal = uiState.todayKcal,
                    workoutsCount = uiState.todayWorkoutsCount,
                    onClick = onHistoryClick
                )
            }

            // 5. Workout Cards Grid
            items(uiState.workouts, key = { it.id }) { workout ->
                WorkoutCard(
                    workout = workout,
                    onToggle = { enabled -> viewModel.toggleWorkout(workout.id, enabled) },
                    onClick = { onWorkoutClick(workout.id) }
                )
            }
        }

        // Fixed Bottom Dock
        DashboardDock(
            onHistoryClick = onHistoryClick,
            onQuickStartClick = onQuickStartClick,
            onSettingsClick = onSettingsClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Weather Details Bottom Sheet
        if (uiState.showWeatherDetails && uiState.weatherInfo != null) {
            val weather = uiState.weatherInfo!!
            WeatherDetailsBottomSheet(
                weather = weather,
                onDismiss = { viewModel.dismissWeatherDetails() }
            )
        }

        // Add Workout Dialog
        if (uiState.showAddWorkoutDialog) {
            AddWorkoutDialog(
                onDismiss = { viewModel.dismissAddWorkoutDialog() },
                onConfirm = { name, cat, dur ->
                    viewModel.addCustomWorkout(name, cat, dur)
                }
            )
        }
    }
}

/**
 * Composite Weather Card featuring:
 * - Lower metrics strip (Sensible, Humidity, Pressure)
 * - Main white neumorphic card with Sun & Cloud vector
 * - Interactive TODAY / TOMORROW tab notch
 */
@Composable
private fun WeatherCompositeCard(
    isToday: Boolean,
    temp: Int,
    lowTemp: Int,
    description: String,
    sensible: Int,
    humidity: Int,
    pressure: Int,
    onToggleTab: () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        // Lower Weather Metrics Strip (sits behind bottom half)
        Box(
            modifier = Modifier
                .offset(y = 158.dp)
                .size(width = 305.dp, height = 93.dp)
                .clip(FitShapes.WeatherStrip)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFECEFF6), Color(0xFFF6F8FC))
                    )
                )
                .neumorphicRaised(
                    shape = FitShapes.WeatherStrip,
                    darkShadowColor = FitColors.ShadowDarkSubtle,
                    lightShadowColor = Color.Transparent,
                    darkBlur = 14.dp
                )
                .padding(top = 34.dp, start = 12.dp, end = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherMetricItem(value = "${sensible}°", label = "Sensible")
                WeatherMetricItem(value = "${humidity}%", label = "Humidity")
                WeatherMetricItem(value = "$pressure hPa", label = "Pressure")
            }
        }

        // Main Weather Card (width 393dp, height 186dp, radius 40dp)
        Box(
            modifier = Modifier
                .size(width = 393.dp, height = 186.dp)
                .neumorphicRaised(
                    shape = FitShapes.WeatherCardShape,
                    darkShadowColor = FitColors.ShadowDark,
                    lightShadowColor = Color.White.copy(alpha = 0.9f),
                    darkBlur = 24.dp
                )
                .clip(FitShapes.WeatherCardShape)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White, Color(0xFFFAFBFF))
                    )
                )
        ) {
            // TODAY Notch Tab (top center)
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(width = 128.dp, height = 30.dp)
                    .clip(FitShapes.TodayNotch)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFE8ECF4), Color(0xFFF2F4F9))
                        )
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onToggleTab
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isToday) "TODAY" else "TOMORROW",
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.4.sp,
                    color = Color(0xFF3C4357)
                )
            }

            // Left Sun & Cloud Vector
            WeatherIllustration(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 18.dp, y = (-12).dp)
            )

            // Right Temperature and Description Info
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 28.dp, top = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$temp",
                        fontFamily = Poppins,
                        fontSize = 62.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-1).sp,
                        color = Color(0xFF1A2033)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // 1px Vertical divider
                    Box(
                        modifier = Modifier
                            .size(width = 1.dp, height = 46.dp)
                            .background(Color(0xFFD5DAE5))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "${lowTemp}°",
                        fontFamily = Poppins,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFFB4BBCA)
                    )
                }

                Text(
                    text = description,
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    color = Color(0xFFC3C9D5),
                    modifier = Modifier.offset(y = (-4).dp)
                )
            }
        }
    }
}

@Composable
private fun WeatherMetricItem(
    value: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontFamily = Poppins,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C3346)
        )
        Text(
            text = label,
            fontFamily = Poppins,
            fontSize = 12.sp,
            color = Color(0xFFC0C6D3)
        )
    }
}

/**
 * Energy / Calories Pill:
 * Outer inset well + Green gradient action button + Metrics
 */
@Composable
private fun CaloriesEnergyPill(
    kcal: Int,
    workoutsCount: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .clip(FitShapes.EnergyPillOuter)
            .background(Color(0xFFEEF1F7))
            .neumorphicInset(
                shape = FitShapes.EnergyPillOuter,
                topShadowColor = Color(0x66AAB8D0),
                bottomShadowColor = Color.White
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 5.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Green Pill Action
            Box(
                modifier = Modifier
                    .size(width = 160.dp, height = 76.dp)
                    .neumorphicRaised(
                        shape = FitShapes.EnergyPillInner,
                        darkShadowColor = FitColors.GreenPillGlow,
                        lightShadowColor = Color.Transparent,
                        darkBlur = 20.dp
                    )
                    .clip(FitShapes.EnergyPillInner)
                    .background(
                        Brush.linearGradient(
                            listOf(FitColors.GreenPillStart, FitColors.GreenPillEnd)
                        )
                    )
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Calories",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Calories",
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Right side stats
            Column {
                Text(
                    text = "$kcal kcal",
                    fontFamily = Poppins,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2A3144)
                )
                Text(
                    text = "$workoutsCount workouts done",
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = Color(0xFFBCC3D0)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeatherDetailsBottomSheet(
    weather: com.fithome.app.data.model.WeatherInfo,
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
                text = "Outdoor Weather Insight",
                fontFamily = Poppins,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = FitColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Optimal running conditions! Low humidity (${weather.humidityPercent}%) and comfortable ambient temperature (${weather.temperature}°C). Recommended: Stay hydrated with 500ml water before starting.",
                fontFamily = Poppins,
                fontSize = 15.sp,
                color = FitColors.TextSecondary,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AddWorkoutDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, category: String, duration: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Cardio") }
    var duration by remember { mutableStateOf("45") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Custom Workout",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                color = FitColors.TextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Workout Name (e.g. Boxing)", fontFamily = Poppins) },
                    singleLine = true
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Cardio", "Strength", "Yoga").forEach { cat ->
                        val isSel = cat == category
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) FitColors.BlueTabEnd else Color(0xFFEEF1F7))
                                .clickable { category = cat }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = cat,
                                fontFamily = Poppins,
                                fontSize = 12.sp,
                                color = if (isSel) Color.White else FitColors.TextSecondary
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duration (minutes)", fontFamily = Poppins) },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        val dur = duration.toIntOrNull() ?: 45
                        onConfirm(name.trim(), category, dur)
                    }
                }
            ) {
                Text("Add", fontFamily = Poppins, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", fontFamily = Poppins)
            }
        }
    )
}
