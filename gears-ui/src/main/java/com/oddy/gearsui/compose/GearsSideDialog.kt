package com.oddy.gearsui.compose

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun GearsSideDialog(
    sideDialogContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    sideDialogState: GearsSideDialogState = rememberGearsSideDialogState(DrawerValue.Closed),
    gesturesEnabled: Boolean = true,
    sideDialogBackgroundColor: Color = MaterialTheme.colors.surface,
    drawerWidth: Dp? = null,
    scrimColor: Color = DrawerDefaults.scrimColor,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val widthInPixel = with(LocalDensity.current) { drawerWidth?.toPx() }

    BoxWithConstraints(modifier.fillMaxSize()) {
        val modalDrawerConstraints = constraints
        if (!modalDrawerConstraints.hasBoundedWidth) {
            throw IllegalStateException("Drawer shouldn't have infinite width")
        }

        val closedValue = modalDrawerConstraints.maxWidth.toFloat()
        val openedValue = widthInPixel?.let { closedValue - it } ?: 0f
        val anchors = mapOf(closedValue to DrawerValue.Closed, openedValue to DrawerValue.Open)

        Box(
            Modifier.gearsSwipeable(
                state = sideDialogState.swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal,
                enabled = gesturesEnabled,
                velocityThreshold = DrawerVelocityThreshold,
                resistance = null
            )
        ) {
            Box {
                content()
            }
            GearsScrim(
                open = sideDialogState.isOpen,
                onClose = {
                    if (
                        gesturesEnabled &&
                        sideDialogState.swipeableState.confirmStateChange(DrawerValue.Closed)
                    ) {
                        scope.launch { sideDialogState.close() }
                    }
                },
                fraction = {
                    calculateFraction(closedValue, openedValue, sideDialogState.offset.value)
                },
                color = scrimColor
            )

            val paddingModifier =
                if (drawerWidth == null) Modifier.padding(start = 56.dp) else Modifier

            Surface(
                modifier = Modifier
                    .offset { IntOffset(sideDialogState.offset.value.roundToInt(), 0) }
                    .then(paddingModifier)
                    .semantics {
                        if (sideDialogState.isOpen) {
                            dismiss {
                                if (sideDialogState.swipeableState.confirmStateChange(DrawerValue.Closed)) {
                                    scope.launch { sideDialogState.close() }
                                }
                                true
                            }
                        }
                    },
                color = sideDialogBackgroundColor,
                elevation = DrawerDefaults.Elevation
            ) {
                val contentModifier =
                    if (drawerWidth != null) Modifier.width(drawerWidth) else Modifier.fillMaxWidth()
                Column(modifier = contentModifier, content = sideDialogContent)
            }
        }
    }
}

@Stable
@ExperimentalMaterialApi
class GearsSideDialogState(initialValue: DrawerValue) {
    internal val swipeableState = GearsSwipeableState(
        initialValue = initialValue,
        animationSpec = GearsAnimationSpec
    )

    /**
     * Whether the drawer is open.
     */
    val isOpen: Boolean
        get() = currentValue == DrawerValue.Open


    /**
     * Whether the drawer is closed.
     */
    val isClosed: Boolean
        get() = currentValue == DrawerValue.Closed

    /**
     * The current value of the state.
     *
     * If no swipe or animation is in progress, this corresponds to the start the drawer
     * currently in. If a swipe or an animation is in progress, this corresponds the state drawer
     * was in before the swipe or animation started.
     */
    val currentValue: DrawerValue
        get() {
            return swipeableState.currentValue
        }

    /**
     * Open the drawer with animation and suspend until it if fully opened or animation has been
     * cancelled. This method will throw [CancellationException] if the animation is
     * interrupted
     *
     * @return the reason the open animation ended
     */
    suspend fun open() = animateTo(DrawerValue.Open, GearsAnimationSpec)

    /**
     * Close the drawer with animation and suspend until it if fully closed or animation has been
     * cancelled. This method will throw [CancellationException] if the animation is
     * interrupted
     *
     * @return the reason the close animation ended
     */
    suspend fun close() = animateTo(DrawerValue.Closed, GearsAnimationSpec)

    /**
     * Set the state of the drawer with specific animation
     *
     * @param targetValue The new value to animate to.
     * @param anim The animation that will be used to animate to the new value.
     */
    @ExperimentalMaterialApi
    suspend fun animateTo(targetValue: DrawerValue, anim: AnimationSpec<Float>) {
        swipeableState.animateTo(targetValue, anim)
    }

    /**
     * The current position (in pixels) of the drawer sheet.
     */
    @Suppress("OPT_IN_MARKER_ON_WRONG_TARGET")
    @ExperimentalMaterialApi
    @get:ExperimentalMaterialApi
    val offset: State<Float>
        get() = swipeableState.offset

    companion object {
        /**
         * The default [Saver] implementation for [GearsSideDialogState].
         */
        fun Saver(confirmStateChange: (DrawerValue) -> Boolean) =
            Saver<GearsSideDialogState, DrawerValue>(
                save = { it.currentValue },
                restore = { GearsSideDialogState(it) }
            )
    }
}

@ExperimentalMaterialApi
@Composable
fun rememberGearsSideDialogState(
    initialValue: DrawerValue,
    confirmStateChange: (DrawerValue) -> Boolean = { true }
): GearsSideDialogState {
    return rememberSaveable(saver = GearsSideDialogState.Saver(confirmStateChange)) {
        GearsSideDialogState(initialValue)
    }
}

// this is taken from the DrawerLayout's DragViewHelper as a min duration.
private val GearsAnimationSpec = TweenSpec<Float>(durationMillis = 256)
private val DrawerVelocityThreshold = 400.dp

/**
 * Contains useful defaults for [gearsSwipeable] and [SwipeableState].
 */
object GearsSwipeableDefaults {
    /**
     * The default animation used by [SwipeableState].
     */
    val AnimationSpec = SpringSpec<Float>()
}

@Composable
private fun GearsScrim(
    open: Boolean,
    onClose: () -> Unit,
    fraction: () -> Float,
    color: Color
) {
    val dismissDrawer = if (open) {
        Modifier
            .pointerInput(onClose) { detectTapGestures { onClose() } }
            .semantics(mergeDescendants = true) {
                onClick {
                    onClose()
                    true
                }
            }
    } else {
        Modifier
    }

    Canvas(
        Modifier
            .fillMaxSize()
            .then(dismissDrawer)
    ) {
        drawRect(color, alpha = fraction())
    }
}

private fun calculateFraction(a: Float, b: Float, pos: Float) =
    ((pos - a) / (b - a)).coerceIn(0f, 1f)