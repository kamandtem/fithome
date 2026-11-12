package com.fithome.app.ui.screens.notifications

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fithome.app.ui.components.DetailHeader
import com.fithome.app.ui.components.FitShapes
import com.fithome.app.ui.modifiers.neumorphicGlow
import com.fithome.app.ui.modifiers.neumorphicRaised
import com.fithome.app.ui.theme.FitColors
import com.fithome.app.ui.theme.Poppins

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                DetailHeader(
                    title = "Notifications",
                    hasUnreadNotifications = false,
                    onBackClick = onBackClick,
                    onNotificationClick = {}
                )
            }

            // Mark All as Read button row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEEF1F7))
                            .clickable { viewModel.markAllRead() }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Mark all read",
                            fontFamily = Poppins,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = FitColors.BlueTabEnd
                        )
                    }
                }
            }

            // Notifications List
            items(uiState.items, key = { it.id }) { item ->
                NotificationCard(item = item)
            }
        }
    }
}

@Composable
private fun NotificationCard(item: NotificationItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FitShapes.CardShape)
            .background(Color.White)
            .neumorphicRaised(shape = FitShapes.CardShape, darkBlur = 14.dp)
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Unread orange dot
            if (item.isUnread) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .offset(y = 6.dp)
                        .neumorphicGlow(FitColors.OrangeBlobGlow, radius = 4.dp)
                        .clip(CircleShape)
                        .background(FitColors.OrangeBlobMid)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        fontFamily = Poppins,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = FitColors.TextDark
                    )
                    Text(
                        text = item.timeAgo,
                        fontFamily = Poppins,
                        fontSize = 12.sp,
                        color = FitColors.TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = item.message,
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    color = FitColors.TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
