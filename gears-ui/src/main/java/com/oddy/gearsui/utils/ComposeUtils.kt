package com.oddy.gearsui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier

fun Modifier.disableRipple(interactionSource: MutableInteractionSource, onClick: () -> Unit) =
    this.clickable(indication = null, interactionSource = interactionSource, onClick = onClick)