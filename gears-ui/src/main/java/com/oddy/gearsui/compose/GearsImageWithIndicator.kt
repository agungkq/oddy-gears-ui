package com.oddy.gearsui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.oddy.gearsui.R

sealed class GearsImageWithIndicatorVariant(
    val painter: Painter,
    val alignIndicator: Alignment,
    val isIndicatorVisible: Boolean = true
) {
    data class AlignmentTopStart(val vPainter: Painter, val vIsIndicatorVisible: Boolean = true) :
        GearsImageWithIndicatorVariant(vPainter, Alignment.TopStart, vIsIndicatorVisible)

    data class AlignmentTopEnd(val vPainter: Painter, val vIsIndicatorVisible: Boolean = true) :
        GearsImageWithIndicatorVariant(vPainter, Alignment.TopEnd, vIsIndicatorVisible)

    data class AlignmentTopCenter(val vPainter: Painter, val vIsIndicatorVisible: Boolean = true) :
        GearsImageWithIndicatorVariant(vPainter, Alignment.TopCenter, vIsIndicatorVisible)

    data class AlignmentBottomStart(val vPainter: Painter, val vIsIndicatorVisible: Boolean = true) :
        GearsImageWithIndicatorVariant(vPainter, Alignment.BottomStart, vIsIndicatorVisible)

    data class AlignmentBottomEnd(val vPainter: Painter, val vIsIndicatorVisible: Boolean = true) :
        GearsImageWithIndicatorVariant(vPainter, Alignment.BottomEnd, vIsIndicatorVisible)

    data class AlignmentBottomCenter(val vPainter: Painter, val vIsIndicatorVisible: Boolean = true) :
        GearsImageWithIndicatorVariant(vPainter, Alignment.BottomCenter, vIsIndicatorVisible)
}

@Composable
fun GearsImageWithIndicator(
    modifier: Modifier = Modifier,
    variant: GearsImageWithIndicatorVariant,
    count: Int? = null,
) {
    Box(modifier = modifier) {
        Image(painter = variant.painter, contentDescription = "image")
        if (variant.isIndicatorVisible) CountIndicator(
            modifier = Modifier.align(variant.alignIndicator),
            count = count
        )
    }
}

@Preview
@Composable
fun GearsImageWithIndicatorPreview() {
    Column {
        Row {
            GearsImageWithIndicator(
                variant = GearsImageWithIndicatorVariant.AlignmentTopStart(painterResource(id = R.drawable.ic_notification))
            )

            GearsImageWithIndicator(
                variant = GearsImageWithIndicatorVariant.AlignmentTopStart(painterResource(id = R.drawable.ic_notification)),
                count = 99
            )
        }

        Row {
            GearsImageWithIndicator(
                variant = GearsImageWithIndicatorVariant.AlignmentTopCenter(painterResource(id = R.drawable.ic_notification))
            )

            GearsImageWithIndicator(
                variant = GearsImageWithIndicatorVariant.AlignmentTopCenter(painterResource(id = R.drawable.ic_notification)),
                count = 99
            )
        }

        Row {
            GearsImageWithIndicator(
                variant = GearsImageWithIndicatorVariant.AlignmentTopEnd(painterResource(id = R.drawable.ic_notification))
            )

            GearsImageWithIndicator(
                variant = GearsImageWithIndicatorVariant.AlignmentTopEnd(painterResource(id = R.drawable.ic_notification)),
                count = 99
            )
        }


        Row {
            GearsImageWithIndicator(
                variant = GearsImageWithIndicatorVariant.AlignmentBottomStart(painterResource(id = R.drawable.ic_notification))
            )

            GearsImageWithIndicator(
                variant = GearsImageWithIndicatorVariant.AlignmentBottomStart(painterResource(id = R.drawable.ic_notification)),
                count = 99
            )
        }

        Row {
            GearsImageWithIndicator(
                variant = GearsImageWithIndicatorVariant.AlignmentBottomCenter(painterResource(id = R.drawable.ic_notification))
            )

            GearsImageWithIndicator(
                variant = GearsImageWithIndicatorVariant.AlignmentBottomCenter(painterResource(id = R.drawable.ic_notification)),
                count = 99
            )
        }

        Row {
            GearsImageWithIndicator(
                variant = GearsImageWithIndicatorVariant.AlignmentBottomEnd(painterResource(id = R.drawable.ic_notification))
            )

            GearsImageWithIndicator(
                variant = GearsImageWithIndicatorVariant.AlignmentBottomEnd(painterResource(id = R.drawable.ic_notification)),
                count = 99
            )
        }

        Row {
            GearsImageWithIndicator(
                variant = GearsImageWithIndicatorVariant.AlignmentBottomEnd(
                    painterResource(id = R.drawable.ic_notification),
                    vIsIndicatorVisible = false
                )
            )
        }
    }
}