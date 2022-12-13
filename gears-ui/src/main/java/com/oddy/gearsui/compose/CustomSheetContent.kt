package com.oddy.gearsui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun CustomSheetContent(content: @Composable () -> Unit) {
    val statusBarInset = WindowInsets.statusBars
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    Box(
        modifier = Modifier
            .heightIn(
                min = 1.dp,
                max = screenHeight - statusBarInset.asPaddingValues().calculateTopPadding()
            )
    ) {
        content()
    }
}