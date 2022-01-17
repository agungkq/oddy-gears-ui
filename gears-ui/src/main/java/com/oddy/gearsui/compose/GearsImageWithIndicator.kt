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

sealed class GearsImageWithIndicatorVariant(val painter: Painter, val alignDot: Alignment) {
    data class AlignmentTopStart(val vPainter: Painter) :
        GearsImageWithIndicatorVariant(vPainter, Alignment.TopStart)

    data class AlignmentTopEnd(val vPainter: Painter) :
        GearsImageWithIndicatorVariant(vPainter, Alignment.TopEnd)

    data class AlignmentTopCenter(val vPainter: Painter) :
        GearsImageWithIndicatorVariant(vPainter, Alignment.TopCenter)

    data class AlignmentBottomStart(val vPainter: Painter) :
        GearsImageWithIndicatorVariant(vPainter, Alignment.BottomStart)

    data class AlignmentBottomEnd(val vPainter: Painter) :
        GearsImageWithIndicatorVariant(vPainter, Alignment.BottomEnd)

    data class AlignmentBottomCenter(val vPainter: Painter) :
        GearsImageWithIndicatorVariant(vPainter, Alignment.BottomCenter)
}

@Composable
fun GearsImageWithIndicator(
    modifier: Modifier = Modifier,
    variant: GearsImageWithIndicatorVariant,
    count: Int? = null,
) {
    Box(modifier = modifier) {
        Image(painter = variant.painter, contentDescription = "image")
        CountIndicator(modifier = Modifier.align(variant.alignDot), count = count)
    }
}

@Preview
@Composable
fun GearsImageDotPreview() {
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
    }
}