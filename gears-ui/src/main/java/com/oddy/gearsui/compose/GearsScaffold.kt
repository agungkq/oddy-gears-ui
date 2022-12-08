package com.oddy.gearsui.compose

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@ExperimentalMaterialApi
@Stable
class GearsScaffoldState(
    val mainScaffoldState: ScaffoldState,
    val sideDialogState: GearsSideDialogState?,
    val modalBottomSheetState: GearsModalBottomSheetState?
) {
    constructor(
        scaffoldState: ScaffoldState,
        sideDialogState: GearsSideDialogState?
    ) : this(scaffoldState, sideDialogState, null)

    constructor(
        scaffoldState: ScaffoldState,
        modalBottomSheetState: GearsModalBottomSheetState?
    ) : this(scaffoldState, null, modalBottomSheetState)

    val drawerState: DrawerState
        get() = mainScaffoldState.drawerState

    val snackbarHostState: SnackbarHostState
        get() = mainScaffoldState.snackbarHostState

    val isSideDialogOpened: Boolean
        get() = sideDialogState?.isOpen == true

    val isSideDialogClosed: Boolean
        get() = sideDialogState?.isClosed == true

    val isModalBottomSheetVisible: Boolean
        get() = modalBottomSheetState?.isVisible == true

    suspend fun openSideDialog() {
        checkNotNull(sideDialogState)
        sideDialogState.open()
    }

    suspend fun closeSideDialog() {
        checkNotNull(sideDialogState)
        sideDialogState.close()
    }

    suspend fun showModalBottomSheet() {
        checkNotNull(modalBottomSheetState)
        modalBottomSheetState.show()
    }

    suspend fun hideModalBottomSheet() {
        checkNotNull(modalBottomSheetState)
        modalBottomSheetState.hide()
    }
}

@ExperimentalMaterialApi
@Composable
fun rememberGearsScaffoldState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    sideDialogState: GearsSideDialogState
): GearsScaffoldState = remember {
    GearsScaffoldState(scaffoldState, sideDialogState)
}

@ExperimentalMaterialApi
@Composable
fun rememberGearsScaffoldState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    modalBottomSheetState: GearsModalBottomSheetState
): GearsScaffoldState = remember {
    GearsScaffoldState(scaffoldState, modalBottomSheetState)
}

@ExperimentalMaterialApi
@Composable
fun rememberGearsScaffoldState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    modalBottomSheetState: GearsModalBottomSheetState,
    sideDialogState: GearsSideDialogState
): GearsScaffoldState = remember {
    GearsScaffoldState(scaffoldState, sideDialogState, modalBottomSheetState)
}

@Composable
fun GearsScaffold(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    drawerContent: @Composable (ColumnScope.() -> Unit)? = null,
    drawerGesturesEnabled: Boolean = true,
    drawerShape: Shape = MaterialTheme.shapes.large,
    drawerElevation: Dp = DrawerDefaults.Elevation,
    drawerBackgroundColor: Color = MaterialTheme.colors.surface,
    drawerContentColor: Color = contentColorFor(drawerBackgroundColor),
    drawerScrimColor: Color = DrawerDefaults.scrimColor,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.imePadding(),
        scaffoldState = scaffoldState,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        isFloatingActionButtonDocked = isFloatingActionButtonDocked,
        drawerContent = drawerContent,
        drawerGesturesEnabled = drawerGesturesEnabled,
        drawerShape = drawerShape,
        drawerElevation = drawerElevation,
        drawerBackgroundColor = drawerBackgroundColor,
        drawerContentColor = drawerContentColor,
        drawerScrimColor = drawerScrimColor,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        content = content
    )
}

@ExperimentalMaterialApi
@Composable
fun GearsScaffold(
    modifier: Modifier = Modifier,
    gearsScaffoldState: GearsScaffoldState = rememberGearsScaffoldState(
        sideDialogState = rememberGearsSideDialogState(initialValue = DrawerValue.Closed)
    ),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    drawerContent: @Composable (ColumnScope.() -> Unit)? = null,
    drawerGesturesEnabled: Boolean = true,
    drawerShape: Shape = MaterialTheme.shapes.large,
    drawerElevation: Dp = DrawerDefaults.Elevation,
    drawerBackgroundColor: Color = MaterialTheme.colors.surface,
    drawerContentColor: Color = contentColorFor(drawerBackgroundColor),
    drawerScrimColor: Color = DrawerDefaults.scrimColor,
    sideDialogContent: @Composable ColumnScope.() -> Unit,
    sideDialogWidth: Dp? = null,
    sideDialogBackgroundColor: Color = MaterialTheme.colors.surface,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable (PaddingValues) -> Unit
) {
    requireNotNull(gearsScaffoldState.sideDialogState) {
        "GearsScaffoldState.GearsSideDialogState can not be null!"
    }

    GearsSideDialog(
        modifier = modifier.imePadding(),
        sideDialogState = gearsScaffoldState.sideDialogState,
        gesturesEnabled = gearsScaffoldState.sideDialogState.isOpen,
        sideDialogContent = sideDialogContent,
        sideDialogBackgroundColor = sideDialogBackgroundColor,
        drawerWidth = sideDialogWidth,
        content = {
            GearsScaffold(
                modifier = Modifier,
                scaffoldState = gearsScaffoldState.mainScaffoldState,
                snackbarHost = snackbarHost,
                topBar = topBar,
                bottomBar = bottomBar,
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = floatingActionButtonPosition,
                isFloatingActionButtonDocked = isFloatingActionButtonDocked,
                drawerContent = drawerContent,
                drawerGesturesEnabled = drawerGesturesEnabled,
                drawerShape = drawerShape,
                drawerElevation = drawerElevation,
                drawerBackgroundColor = drawerBackgroundColor,
                drawerContentColor = drawerContentColor,
                drawerScrimColor = drawerScrimColor,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                content = content
            )
        }
    )
}


@ExperimentalMaterialApi
@Composable
fun GearsScaffold(
    modifier: Modifier = Modifier,
    gearsScaffoldState: GearsScaffoldState = rememberGearsScaffoldState(
        modalBottomSheetState = rememberGearsModalBottomSheetState(initialValue = GearsModalBottomSheetValue.Hidden)
    ),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    drawerContent: @Composable() (ColumnScope.() -> Unit)? = null,
    drawerGesturesEnabled: Boolean = true,
    drawerShape: Shape = MaterialTheme.shapes.large,
    drawerElevation: Dp = DrawerDefaults.Elevation,
    drawerBackgroundColor: Color = MaterialTheme.colors.surface,
    drawerContentColor: Color = contentColorFor(drawerBackgroundColor),
    drawerScrimColor: Color = DrawerDefaults.scrimColor,
    sheetContent: @Composable () -> Unit,
    sheetBackgroundColor: Color = MaterialTheme.colors.surface,
    sheetShape: Shape = MaterialTheme.shapes.large,
    sheetElevation: Dp = ModalBottomSheetDefaults.Elevation,
    isSheetDraggable: Boolean = true,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable (PaddingValues) -> Unit
) {
    requireNotNull(gearsScaffoldState.modalBottomSheetState) {
        "GearsScaffoldState.ModalBottomSheetState can not be null!"
    }

    GearsModalBottomSheetLayout(
        sheetContent = sheetContent,
        modifier = modifier.imePadding(),
        sheetState = gearsScaffoldState.modalBottomSheetState,
        sheetShape = sheetShape,
        sheetElevation = sheetElevation,
        sheetBackgroundColor = sheetBackgroundColor,
        showHandlebar = isSheetDraggable,
        content = {
            GearsScaffold(
                modifier = Modifier,
                scaffoldState = gearsScaffoldState.mainScaffoldState,
                snackbarHost = snackbarHost,
                topBar = topBar,
                bottomBar = bottomBar,
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = floatingActionButtonPosition,
                isFloatingActionButtonDocked = isFloatingActionButtonDocked,
                drawerContent = drawerContent,
                drawerGesturesEnabled = drawerGesturesEnabled,
                drawerShape = drawerShape,
                drawerElevation = drawerElevation,
                drawerBackgroundColor = drawerBackgroundColor,
                drawerContentColor = drawerContentColor,
                drawerScrimColor = drawerScrimColor,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                content = content
            )
        }
    )
}


@ExperimentalMaterialApi
@Composable
fun GearsScaffold(
    modifier: Modifier = Modifier,
    gearsScaffoldState: GearsScaffoldState = rememberGearsScaffoldState(
        sideDialogState = rememberGearsSideDialogState(initialValue = DrawerValue.Closed),
        modalBottomSheetState = rememberGearsModalBottomSheetState(initialValue = GearsModalBottomSheetValue.Hidden)
    ),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    drawerContent: @Composable (ColumnScope.() -> Unit)? = null,
    drawerGesturesEnabled: Boolean = true,
    drawerShape: Shape = MaterialTheme.shapes.large,
    drawerElevation: Dp = DrawerDefaults.Elevation,
    drawerBackgroundColor: Color = MaterialTheme.colors.surface,
    drawerContentColor: Color = contentColorFor(drawerBackgroundColor),
    drawerScrimColor: Color = DrawerDefaults.scrimColor,
    sideDialogContent: @Composable ColumnScope.() -> Unit,
    sideDialogWidth: Dp? = null,
    sideDialogBackgroundColor: Color = MaterialTheme.colors.surface,
    sheetContent: @Composable () -> Unit,
    sheetBackgroundColor: Color = MaterialTheme.colors.surface,
    sheetShape: Shape = MaterialTheme.shapes.large,
    sheetElevation: Dp = ModalBottomSheetDefaults.Elevation,
    isSheetDraggable: Boolean = true,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable (PaddingValues) -> Unit
) {
    requireNotNull(gearsScaffoldState.modalBottomSheetState) {
        "GearsScaffoldState.ModalBottomSheetState can not be null!"
    }

    requireNotNull(gearsScaffoldState.sideDialogState) {
        "GearsScaffoldState.GearsSideDialogState can not be null!"
    }

    GearsSideDialog(
        modifier = modifier.imePadding(),
        sideDialogState = gearsScaffoldState.sideDialogState,
        gesturesEnabled = gearsScaffoldState.sideDialogState.isOpen,
        sideDialogContent = sideDialogContent,
        sideDialogBackgroundColor = sideDialogBackgroundColor,
        drawerWidth = sideDialogWidth,
        content = {
            GearsModalBottomSheetLayout(
                sheetContent = sheetContent,
                sheetState = gearsScaffoldState.modalBottomSheetState,
                sheetShape = sheetShape,
                sheetElevation = sheetElevation,
                sheetBackgroundColor = sheetBackgroundColor,
                showHandlebar = isSheetDraggable,
                content = {
                    GearsScaffold(
                        modifier = Modifier,
                        scaffoldState = gearsScaffoldState.mainScaffoldState,
                        snackbarHost = snackbarHost,
                        topBar = topBar,
                        bottomBar = bottomBar,
                        floatingActionButton = floatingActionButton,
                        floatingActionButtonPosition = floatingActionButtonPosition,
                        isFloatingActionButtonDocked = isFloatingActionButtonDocked,
                        drawerContent = drawerContent,
                        drawerGesturesEnabled = drawerGesturesEnabled,
                        drawerShape = drawerShape,
                        drawerElevation = drawerElevation,
                        drawerBackgroundColor = drawerBackgroundColor,
                        drawerContentColor = drawerContentColor,
                        drawerScrimColor = drawerScrimColor,
                        backgroundColor = backgroundColor,
                        contentColor = contentColor,
                        content = content
                    )
                }
            )
        }
    )
}