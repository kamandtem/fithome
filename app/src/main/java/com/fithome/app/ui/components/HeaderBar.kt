package com.fithome.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.fithome.app.ui.modifiers.neumorphicButton
import com.fithome.app.ui.modifiers.neumorphicGlow
import com.fithome.app.ui.theme.FitColors
import com.fithome.app.ui.theme.Poppins

/**
 * Standard Header for Detail / Session screens.
 */
@Composable
fun DetailHeader(
    title: String,
    hasUnreadNotifications: Boolean,
    onBackClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Back Button
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(52.dp)
                .neumorphicButton(shape = CircleShape, onClick = onBackClick)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(Color.White, Color(0xFFF1F4F9)))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF3B4357),
                modifier = Modifier.size(18.dp)
            )
        }

        // Title
        Text(
            text = title,
            fontFamily = Poppins,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = FitColors.TextPrimary
        )

        // Notification Orange Blob Button
        NotificationBlobButton(
            hasUnread = hasUnreadNotifications,
            onClick = onNotificationClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

/**
 * Dashboard Profile Header with avatar, "+" badge, greeting and notification button.
 */
@Composable
fun DashboardHeader(
    userName: String,
    hasUnreadNotifications: Boolean,
    onProfileClick: () -> Unit,
    onAddPhotoClick: () -> Unit,
    onNotificationClick: () -> Unit,
    avatarContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar with + badge
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(Color(0xFFD9DDE6), Color(0xFFAAB1C0)))
                )
                .padding(3.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onProfileClick
                )
        ) {
            avatarContent()

            // "+" badge
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1D8CF8))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onAddPhotoClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Change photo",
                    tint = Color.White,
                    modifier = Modifier.size(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Greeting and Subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                        append("Hey, ")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(userName)
                    }
                },
                fontFamily = Poppins,
                fontSize = 21.sp,
                color = FitColors.TextPrimary
            )
            Text(
                text = "Let's crush today's goals",
                fontFamily = Poppins,
                fontSize = 13.sp,
                color = FitColors.TextMuted
            )
        }

        // Notification button
        NotificationBlobButton(
            hasUnread = hasUnreadNotifications,
            onClick = onNotificationClick
        )
    }
}

/**
 * The glowing orange blob notification button.
 */
@Composable
fun NotificationBlobButton(
    hasUnread: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(52.dp)
            .neumorphicButton(shape = CircleShape, onClick = onClick)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(listOf(Color.White, Color(0xFFF1F4F9)))
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = 26.dp, height = 22.dp)
                .then(
                    if (hasUnread) {
                        Modifier.neumorphicGlow(FitColors.OrangeBlobGlow, radius = 8.dp)
                    } else Modifier
                )
                .clip(CircleShape)
                .background(
                    if (hasUnread) {
                        Brush.radialGradient(
                            listOf(
                                FitColors.OrangeBlobStart,
                                FitColors.OrangeBlobMid,
                                FitColors.OrangeBlobEnd
                            )
                        )
                    } else {
                        Brush.radialGradient(
                            listOf(Color(0xFFD6DBE5), Color(0xFFB5BDCC))
                        )
                    }
                )
        )
    }
}
