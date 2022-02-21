package com.oddy.customer.onboarding.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets

@Composable
fun CustomSheetContent(content: @Composable () -> Unit) {
    val insets = LocalWindowInsets.current
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    Box(
        modifier = Modifier.heightIn(
            min = 1.dp,
            max = screenHeight - insets.statusBars.bottom.dp
        )
    ) {
        content()
    }
}