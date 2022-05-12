package com.oddy.gearsui.animation

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oddy.gearsui.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ShakingAnimation(startAnimation: Boolean, content: @Composable () -> Unit) {
    var enableAnimation by remember { mutableStateOf(startAnimation) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(startAnimation) {
        enableAnimation = startAnimation
        if (enableAnimation) {
            scope.launch {
                delay(500)
                enableAnimation = false
            }
        }
    }

    if (enableAnimation) {
        val infiniteTransition = rememberInfiniteTransition()
        val shakingState = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 100,
                    easing = LinearEasing
                )
            )
        )

        BoxWithConstraints {
            Box(
                modifier = Modifier.offset(
                    y =
                    when {
                        shakingState.value == 0f -> 0.dp
                        shakingState.value < 0.5f -> {
                            10.dp + (10.dp * shakingState.value)
                        }
                        else -> {
                            10.dp - (10.dp * shakingState.value)
                        }
                    }
                )
            ) { content() }
        }
    } else {
        Box(modifier = Modifier.offset(y = 0.dp)) {
            content()
        }
    }
}

@Preview
@Composable
fun ShakingAnimationPreview() {
    var start by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.black_900))
            .clickable { start = !start }
    ) {
        ShakingAnimation(startAnimation = start) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(color = colorResource(id = R.color.white))
            )
        }
    }
}