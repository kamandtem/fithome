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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.fithome.app.ui.modifiers.neumorphicButton
import com.fithome.app.ui.modifiers.neumorphicRaised
import com.fithome.app.ui.theme.FitColors
import com.fithome.app.ui.theme.Poppins

@Composable
fun StepperDialog(
    initialMinutes: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var minutes by remember { mutableIntStateOf(initialMinutes) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(FitColors.BgTop, FitColors.BgMid)
                    )
                )
                .neumorphicRaised(shape = RoundedCornerShape(32.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Workout Duration",
                    fontFamily = Poppins,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FitColors.TextPrimary
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Stepper Row (- value +)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .neumorphicButton(shape = CircleShape) {
                                if (minutes > 5) minutes -= 5
                            }
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease duration",
                            tint = FitColors.TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    Text(
                        text = "${minutes}m",
                        fontFamily = Poppins,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = FitColors.TextDark
                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .neumorphicButton(shape = CircleShape) {
                                if (minutes < 120) minutes += 5
                            }
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase duration",
                            tint = FitColors.TextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Quick preset pills (15, 30, 45, 60)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(15, 30, 45, 60).forEach { preset ->
                        val isSelected = minutes == preset
                        Box(
                            modifier = Modifier
                                .neumorphicButton(shape = RoundedCornerShape(12.dp)) {
                                    minutes = preset
                                }
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) {
                                        Brush.linearGradient(
                                            listOf(FitColors.BlueTabStart, FitColors.BlueTabEnd)
                                        )
                                    } else {
                                        Brush.linearGradient(listOf(Color.White, Color.White))
                                    }
                                )
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${preset}m",
                                fontFamily = Poppins,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) Color.White else FitColors.TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .neumorphicButton(shape = RoundedCornerShape(24.dp), onClick = onDismiss)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancel",
                            fontFamily = Poppins,
                            fontSize = 15.sp,
                            color = FitColors.TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .neumorphicButton(shape = RoundedCornerShape(24.dp)) {
                                onConfirm(minutes)
                            }
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(FitColors.BlueTabStart, FitColors.BlueTabEnd)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Set",
                            fontFamily = Poppins,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
