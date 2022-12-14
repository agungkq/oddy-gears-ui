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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.oddy.gearsui.R
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
    onFirstLoad: @Composable LazyItemScope.() -> Unit = {
        Box(
            modifier = Modifier.fillParentMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = colorResource(id = R.color.dark_cyan))
        }
    },
    onEmptyLoad: (@Composable () -> Unit)? = null,
    onLoading: @Composable (LazyItemScope.() -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {
    val isLoadedEmpty = with(itemsToListenState) {
        itemCount == 0 && loadState.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached
    }

    if (isLoadedEmpty && onEmptyLoad != null) {
        onEmptyLoad()
    } else {
        LazyColumn(
            modifier,
            state,
            contentPadding,
            reverseLayout,
            verticalArrangement,
            horizontalAlignment,
            flingBehavior
        ) {
            itemsToListenState.apply {
                if (loadState.refresh is LoadState.NotLoading || onLoading == null) content()

                val isLoading = loadState.refresh == LoadState.Loading
                val isFirstLoading = itemCount == 0 && isLoading

                when {
                    loadState.append is LoadState.Loading -> item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )

                            GearsText(
                                modifier = Modifier.padding(start = 8.dp),
                                text = stringResource(id = R.string.loading),
                                type = GearsTextType.Body14,
                                textColor = colorResource(id = R.color.black_500)
                            )
                        }
                    }
                    isFirstLoading -> item {
                        onFirstLoad()
                    }
                    isLoading -> onLoading?.let {
                        item { it() }
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
}