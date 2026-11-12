package com.fithome.app.ui.components

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Avatar View: Displays user custom image from URI or renders the exact vector portrait from the HTML design.
 */
@Composable
fun AvatarView(
    avatarUri: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val customBitmap = remember(avatarUri) {
        if (!avatarUri.isNullOrBlank()) {
            try {
                val input = context.contentResolver.openInputStream(Uri.parse(avatarUri))
                BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    Box(
        modifier = modifier
            .size(52.dp)
            .clip(CircleShape)
    ) {
        if (customBitmap != null) {
            Image(
                bitmap = customBitmap.asImageBitmap(),
                contentDescription = "User Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Exact Vector Port of HTML mockup avatar
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val sx = w / 52f
                val sy = h / 52f

                // Background
                drawRect(Color(0xFFC9CDD5))

                // Shoulders / Clothes: d="M4 52c1-10 9-14 22-14s21 4 22 14z"
                val shoulderPath = Path().apply {
                    moveTo(4f * sx, 52f * sy)
                    cubicTo(5f * sx, 42f * sy, 13f * sx, 38f * sy, 26f * sx, 38f * sy)
                    cubicTo(39f * sx, 38f * sy, 47f * sx, 42f * sy, 48f * sx, 52f * sy)
                    close()
                }
                drawPath(shoulderPath, Color(0xFF3D4352))

                // Neck: rect x="22" y="30" width="8" height="9"
                drawRect(
                    color = Color(0xFFD9A384),
                    topLeft = Offset(22f * sx, 30f * sy),
                    size = Size(8f * sx, 9f * sy)
                )

                // Face: ellipse cx="26" cy="22" rx="9" ry="10.5"
                drawOval(
                    color = Color(0xFFECBFA3),
                    topLeft = Offset((26f - 9f) * sx, (22f - 10.5f) * sy),
                    size = Size(18f * sx, 21f * sy)
                )

                // Hair: d="M16 20c0-9 6-12 11-12s10 3 10 11c-2-4-5-6-10-6s-8 2-11 7z"
                val hairPath = Path().apply {
                    moveTo(16f * sx, 20f * sy)
                    cubicTo(16f * sx, 11f * sy, 22f * sx, 8f * sy, 27f * sx, 8f * sy)
                    cubicTo(32f * sx, 8f * sy, 37f * sx, 11f * sy, 37f * sx, 19f * sy)
                    cubicTo(35f * sx, 15f * sy, 32f * sx, 13f * sy, 27f * sx, 13f * sy)
                    cubicTo(22f * sx, 13f * sy, 19f * sx, 15f * sy, 16f * sx, 20f * sy)
                    close()
                }
                drawPath(hairPath, Color(0xFF6B4630))
            }
        }
    }
}
