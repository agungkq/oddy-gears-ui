package com.oddy.gearsui.compose

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @param coroutineScope: Coroutine scope for retry if data failed to fetch
 * @param itemsToListenState: Main items to listen the state
 */
@Composable
fun GearsLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    itemsToListenState: LazyPagingItems<*>,
    onFirstLoad: @Composable (LazyItemScope.() -> Unit) = {},
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier,
        state,
        contentPadding,
        reverseLayout,
        verticalArrangement,
        horizontalAlignment,
        flingBehavior
    ) {
        content()

        itemsToListenState.apply {
            when {
                loadState.append is LoadState.Loading -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.Center),
                            strokeWidth = 2.dp
                        )
                    }
                }
                loadState.refresh is LoadState.Loading -> item {
                    onFirstLoad()
                }
                loadState.append is LoadState.Error || loadState.refresh is LoadState.Error -> {
                    coroutineScope.launch {
                        delay(5000)
                        retry()
                    }
                }
            }
        }
    }
}

