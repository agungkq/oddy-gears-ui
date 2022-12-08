package com.oddy.gearsui.compose

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.oddy.gearsui.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class GearsModalBottomSheetValue {
    Hidden, Expanded
}

@ExperimentalMaterialApi
class GearsModalBottomSheetState(
    initialValue: GearsModalBottomSheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec
) : GearsSwipeableState<GearsModalBottomSheetValue>(
    initialValue = initialValue,
    animationSpec = animationSpec,
) {
    /**
     * Whether the bottom sheet is visible.
     */
    val isVisible: Boolean
        get() = currentValue != GearsModalBottomSheetValue.Hidden

    /**
     * Show the bottom sheet with animation and suspend until it's shown.
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun show() {
        animateTo(targetValue = GearsModalBottomSheetValue.Expanded)
    }

    /**
     * Hide the bottom sheet with animation and suspend until it if fully hidden or animation has
     * been cancelled.
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun hide() {
        animateTo(targetValue = GearsModalBottomSheetValue.Hidden)
    }

    internal val nestedScrollConnection = this.PreUpPostDownNestedScrollConnection

    companion object {
        /**
         * The default [Saver] implementation for [GearsModalBottomSheetState].
         */
        fun Saver(animationSpec: AnimationSpec<Float>): Saver<GearsModalBottomSheetState, *> =
            Saver(
                save = { it.currentValue },
                restore = {
                    GearsModalBottomSheetState(
                        initialValue = it,
                        animationSpec = animationSpec
                    )
                }
            )
    }
}

/**
 * Create a [GearsModalBottomSheetState] and [remember] it.
 *
 * @param initialValue The initial value of the state.
 * @param animationSpec The default animation that will be used to animate to a new state.
 */
@Composable
@ExperimentalMaterialApi
fun rememberGearsModalBottomSheetState(
    initialValue: GearsModalBottomSheetValue = GearsModalBottomSheetValue.Hidden,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec
): GearsModalBottomSheetState {
    return rememberSaveable(
        initialValue,
        animationSpec,
        saver = GearsModalBottomSheetState.Saver(animationSpec = animationSpec)
    ) {
        GearsModalBottomSheetState(
            initialValue = initialValue,
            animationSpec = animationSpec
        )
    }
}

@Composable
@ExperimentalMaterialApi
fun GearsModalBottomSheetLayout(
    sheetContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: GearsModalBottomSheetState = rememberGearsModalBottomSheetState(),
    sheetShape: Shape = MaterialTheme.shapes.large,
    sheetElevation: Dp = ModalBottomSheetDefaults.Elevation,
    sheetBackgroundColor: Color = MaterialTheme.colors.surface,
    showHandlebar: Boolean = true,
    scrimColor: Color = ModalBottomSheetDefaults.scrimColor,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollModifier = if (showHandlebar) {
        Modifier.nestedScroll(sheetState.nestedScrollConnection)
    } else Modifier

    BoxWithConstraints(modifier) {
        val fullHeight = constraints.maxHeight.toFloat()
        val sheetHeightState = remember { mutableStateOf<Float?>(null) }
        Box(Modifier.fillMaxSize()) {
            content()
            Scrim(
                color = scrimColor,
                onDismiss = {
                    if (sheetState.confirmStateChange(GearsModalBottomSheetValue.Hidden)) {
                        scope.launch { sheetState.hide() }
                    }
                },
                visible = sheetState.targetValue != GearsModalBottomSheetValue.Hidden
            )
        }
        Surface(
            Modifier
                .fillMaxWidth()
                .then(scrollModifier)
                .offset {
                    val y = if (sheetState.anchors.isEmpty()) {
                        // if we don't know our anchors yet, render the sheet as hidden
                        fullHeight.roundToInt()
                    } else {
                        // if we do know our anchors, respect them
                        sheetState.offset.value.roundToInt()
                    }
                    IntOffset(0, y)
                }
                .bottomSheetSwipeable(sheetState, fullHeight, sheetHeightState, showHandlebar)
                .onGloballyPositioned {
                    sheetHeightState.value = it.size.height.toFloat()
                }
                .semantics {
                    if (sheetState.isVisible) {
                        dismiss {
                            if (sheetState.confirmStateChange(GearsModalBottomSheetValue.Hidden)) {
                                scope.launch { sheetState.hide() }
                            }
                            true
                        }
                    }
                },
            shape = sheetShape,
            elevation = sheetElevation,
            color = sheetBackgroundColor,
            contentColor = contentColorFor(sheetBackgroundColor)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (showHandlebar) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .size(40.dp, 4.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(colorResource(id = R.color.monochrome_600))
                            .align(Alignment.CenterHorizontally)
                    )
                }
                sheetContent()
            }
        }
    }
}

@ExperimentalMaterialApi
private fun Modifier.bottomSheetSwipeable(
    sheetState: GearsModalBottomSheetState,
    fullHeight: Float,
    sheetHeightState: State<Float?>,
    isSheetDraggable: Boolean
): Modifier {
    val sheetHeight = sheetHeightState.value
    val modifier = if (sheetHeight != null) {
        val anchors = mapOf(
            fullHeight to GearsModalBottomSheetValue.Hidden,
            fullHeight - sheetHeight to GearsModalBottomSheetValue.Expanded
        )
        Modifier.gearsSwipeable(
            state = sheetState,
            anchors = anchors,
            orientation = Orientation.Vertical,
            enabled = sheetState.currentValue != GearsModalBottomSheetValue.Hidden && isSheetDraggable,
            resistance = null
        )
    } else {
        Modifier
    }

    return this.then(modifier)
}

@Composable
private fun Scrim(
    color: Color,
    onDismiss: () -> Unit,
    visible: Boolean
) {
    if (color.isSpecified) {
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = TweenSpec()
        )
        val dismissModifier = if (visible) {
            Modifier
                .pointerInput(onDismiss) { detectTapGestures { onDismiss() } }
                .semantics(mergeDescendants = true) {
                    onClick {
                        onDismiss()
                        true
                    }
                }
        } else {
            Modifier
        }

        Canvas(
            Modifier
                .fillMaxSize()
                .then(dismissModifier)
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}