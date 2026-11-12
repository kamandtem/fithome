package com.fithome.app.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fithome.app.ui.components.AvatarView
import com.fithome.app.ui.components.DetailHeader
import com.fithome.app.ui.components.FitShapes
import com.fithome.app.ui.modifiers.neumorphicButton
import com.fithome.app.ui.modifiers.neumorphicRaised
import com.fithome.app.ui.theme.FitColors
import com.fithome.app.ui.theme.Poppins

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var nameInput by remember(uiState.name) { mutableStateOf(uiState.name) }
    var weightInput by remember(uiState.weightKg) { mutableStateOf(uiState.weightKg.toString()) }
    var heightInput by remember(uiState.heightCm) { mutableStateOf(uiState.heightCm.toString()) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setAvatarUri(uri.toString())
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            DetailHeader(
                title = "Profile",
                hasUnreadNotifications = false,
                onBackClick = onBackClick,
                onNotificationClick = onNotificationClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Large Avatar with Change Photo Badge
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFFD9DDE6), Color(0xFFAAB1C0)))
                    )
                    .padding(4.dp)
            ) {
                AvatarView(
                    avatarUri = uiState.avatarUri,
                    modifier = Modifier.size(92.dp)
                )

                // Change Photo Camera button
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 2.dp, y = 2.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(FitColors.BlueTabEnd)
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Change photo",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Text(
                text = uiState.name,
                fontFamily = Poppins,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = FitColors.TextDark
            )

            // Neumorphic Input Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FitShapes.CardShape)
                    .background(Color.White)
                    .neumorphicRaised(shape = FitShapes.CardShape, darkBlur = 20.dp)
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Full Name", fontFamily = Poppins) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { weightInput = it },
                        label = { Text("Weight (kg)", fontFamily = Poppins) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = heightInput,
                        onValueChange = { heightInput = it },
                        label = { Text("Height (cm)", fontFamily = Poppins) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Hydration Stats Info Card
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Today's Hydration",
                            fontFamily = Poppins,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FitColors.TextDark
                        )
                        Text(
                            text = "Total water consumed today",
                            fontFamily = Poppins,
                            fontSize = 12.sp,
                            color = FitColors.TextMuted
                        )
                    }

                    Text(
                        text = "${uiState.hydrationMl} ml",
                        fontFamily = Poppins,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = FitColors.WaterDropEnd
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Save Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .neumorphicButton(shape = RoundedCornerShape(27.dp)) {
                        val w = weightInput.toFloatOrNull() ?: 75f
                        val h = heightInput.toIntOrNull() ?: 180
                        viewModel.updateProfile(nameInput.trim(), w, h)
                    }
                    .clip(RoundedCornerShape(27.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(FitColors.BlueTabStart, FitColors.BlueTabEnd)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (uiState.isSavedSuccess) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                    Text(
                        text = if (uiState.isSavedSuccess) "Saved!" else "Save Changes",
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
