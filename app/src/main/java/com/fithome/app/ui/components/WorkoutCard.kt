package com.fithome.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fithome.app.data.model.Workout
import com.fithome.app.ui.modifiers.neumorphicGlow
import com.fithome.app.ui.modifiers.neumorphicRaised
import com.fithome.app.ui.theme.FitColors
import com.fithome.app.ui.theme.Poppins

@Composable
fun WorkoutCard(
    workout: Workout,
    onToggle: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEnabled = workout.enabledToday
    val shape = RoundedCornerShape(26.dp)

    val cardBrush = if (isEnabled) {
        Brush.linearGradient(
            colors = listOf(FitColors.PurpleCardStart, FitColors.PurpleCardEnd)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.White, Color.White)
        )
    }

    val icon: ImageVector = when (workout.iconKey.lowercase()) {
        "cardio", "running" -> Icons.Default.DirectionsRun
        "strength", "gym" -> Icons.Default.FitnessCenter
        "yoga", "flexibility" -> Icons.Default.SelfImprovement
        "cycling", "bike" -> Icons.Default.DirectionsBike
        "swimming" -> Icons.Default.Pool
        else -> Icons.Default.Timer
    }

    Box(
        modifier = modifier
            .size(width = 156.dp, height = 160.dp)
            .then(
                if (isEnabled) {
                    Modifier.neumorphicRaised(
                        shape = shape,
                        darkShadowColor = FitColors.PurpleCardGlow,
                        lightShadowColor = Color.Transparent,
                        darkBlur = 24.dp
                    )
                } else {
                    Modifier.neumorphicRaised(
                        shape = shape,
                        darkShadowColor = Color(0x66AAB9D2),
                        lightShadowColor = Color.White,
                        darkBlur = 18.dp
                    )
                }
            )
            .clip(shape)
            .background(cardBrush)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header Row: Status text + Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isEnabled) "ON" else "OFF",
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isEnabled) Color.White else FitColors.TextMuted
                )

                NeumorphicToggle(
                    checked = isEnabled,
                    onCheckedChange = onToggle,
                    contentDescriptionText = "${workout.name} toggle"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Center Circular Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .then(
                        if (!isEnabled) {
                            Modifier.neumorphicGlow(FitColors.GoldGlow, radius = 12.dp)
                        } else Modifier
                    )
                    .clip(CircleShape)
                    .background(
                        if (isEnabled) Color.White.copy(alpha = 0.12f) else Color.White
                    )
                    .border(
                        width = 2.dp,
                        color = if (isEnabled) Color.White.copy(alpha = 0.55f) else FitColors.GoldLight,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = workout.name,
                    tint = if (isEnabled) Color.White else FitColors.GoldStroke,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Workout Name
            Text(
                text = workout.name,
                fontFamily = Poppins,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isEnabled) Color.White else Color(0xFF262D41)
            )

            // Duration subtitle
            Text(
                text = formatDurationMinutes(workout.minutesToday),
                fontFamily = Poppins,
                fontSize = 13.sp,
                color = if (isEnabled) Color.White.copy(alpha = 0.62f) else FitColors.TextMuted
            )
        }
    }
}

private fun formatDurationMinutes(minutes: Int): String {
    if (minutes <= 0) return "0min"
    val hours = minutes / 60
    val rem = minutes % 60
    return if (hours > 0) {
        if (rem > 0) "${hours}hr ${rem}min" else "${hours}hr"
    } else {
        "${minutes}min"
    }
}
