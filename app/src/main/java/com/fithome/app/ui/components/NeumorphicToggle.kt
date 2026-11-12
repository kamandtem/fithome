package com.fithome.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.fithome.app.ui.modifiers.neumorphicInset
import com.fithome.app.ui.theme.FitColors

@Composable
fun NeumorphicToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentDescriptionText: String = "Toggle"
) {
    val haptic = LocalHapticFeedback.current

    val knobOffset by animateDpAsState(
        targetValue = if (checked) 22.dp else 2.dp,
        animationSpec = tween(durationMillis = 200),
        label = "toggle_knob_offset"
    )

    val bgColor by animateColorAsState(
        targetValue = if (checked) FitColors.ToggleOnBg else FitColors.ToggleOffBg,
        animationSpec = tween(durationMillis = 200),
        label = "toggle_bg_color"
    )

    Box(
        modifier = modifier
            .size(width = 42.dp, height = 22.dp)
            .clip(RoundedCornerShape(11.dp))
            .background(bgColor)
            .then(
                if (!checked) {
                    Modifier.neumorphicInset(
                        shape = RoundedCornerShape(11.dp),
                        topShadowColor = Color(0x5996A5BE),
                        bottomShadowColor = Color.Transparent,
                        depth = 3.dp
                    )
                } else Modifier
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onCheckedChange(!checked)
            }
            .semantics {
                role = Role.Switch
                contentDescription = contentDescriptionText
            }
    ) {
        // Knob (18dp circle)
        Box(
            modifier = Modifier
                .offset(x = knobOffset, y = 2.dp)
                .size(18.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = CircleShape,
                    ambientColor = if (checked) Color(0x593C1478) else Color(0x667887A0),
                    spotColor = if (checked) Color(0x593C1478) else Color(0x667887A0)
                )
                .background(Color.White, CircleShape)
        )
    }
}
