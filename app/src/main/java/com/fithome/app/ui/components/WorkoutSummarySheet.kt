package com.fithome.app.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fithome.app.ui.modifiers.neumorphicButton
import com.fithome.app.ui.modifiers.neumorphicGlow
import com.fithome.app.ui.modifiers.neumorphicRaised
import com.fithome.app.ui.theme.FitColors
import com.fithome.app.ui.theme.Poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSummarySheet(
    workoutName: String,
    durationSec: Int,
    kcal: Int,
    intensity: Int,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = FitColors.BgTop,
        shape = RoundedCornerShape(topStart = 38.dp, topEnd = 38.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Glowing Fire Icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .neumorphicGlow(FitColors.GoldGlow, radius = 16.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color.White, Color(0xFFFDF7EE))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = FitColors.Gold,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Workout Complete!",
                fontFamily = Poppins,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = FitColors.TextPrimary
            )

            Text(
                text = "Great job finishing your $workoutName session.",
                fontFamily = Poppins,
                fontSize = 14.sp,
                color = FitColors.TextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3 Stat Cards in Neumorphic style
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SummaryStatCard(
                    title = "Calories",
                    value = "$kcal",
                    unit = "kcal",
                    modifier = Modifier.weight(1f)
                )
                SummaryStatCard(
                    title = "Time",
                    value = formatSec(durationSec),
                    unit = "",
                    modifier = Modifier.weight(1f)
                )
                SummaryStatCard(
                    title = "Intensity",
                    value = "$intensity",
                    unit = "lvl",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Done Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .neumorphicButton(shape = RoundedCornerShape(27.dp), onClick = onDismiss)
                    .clip(RoundedCornerShape(27.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(FitColors.BlueTabStart, FitColors.BlueTabEnd)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Done",
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SummaryStatCard(
    title: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .neumorphicRaised(shape = RoundedCornerShape(20.dp))
            .padding(vertical = 16.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                fontFamily = Poppins,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = FitColors.TextDark
            )
            if (unit.isNotBlank()) {
                Text(
                    text = unit,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = FitColors.TextMuted
                )
            }
            Text(
                text = title,
                fontFamily = Poppins,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = FitColors.TextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

private fun formatSec(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return if (m > 0) "${m}m ${s}s" else "${s}s"
}
