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

sealed class GearsImageDotVariant(val painter: Painter, val alignDot: Alignment) {
    data class AlignmentTopStart(val vPainter: Painter) :
        GearsImageDotVariant(vPainter, Alignment.TopStart)

    data class AlignmentTopEnd(val vPainter: Painter) :
        GearsImageDotVariant(vPainter, Alignment.TopEnd)

    data class AlignmentTopCenter(val vPainter: Painter) :
        GearsImageDotVariant(vPainter, Alignment.TopCenter)

    data class AlignmentBottomStart(val vPainter: Painter) :
        GearsImageDotVariant(vPainter, Alignment.BottomStart)

    data class AlignmentBottomEnd(val vPainter: Painter) :
        GearsImageDotVariant(vPainter, Alignment.BottomEnd)

    data class AlignmentBottomCenter(val vPainter: Painter) :
        GearsImageDotVariant(vPainter, Alignment.BottomCenter)
}

@Composable
fun GearsImageDot(
    modifier: Modifier = Modifier,
    variant: GearsImageDotVariant,
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
            GearsImageDot(
                variant = GearsImageDotVariant.AlignmentTopStart(painterResource(id = R.drawable.ic_notification))
            )

            GearsImageDot(
                variant = GearsImageDotVariant.AlignmentTopStart(painterResource(id = R.drawable.ic_notification)),
                count = 99
            )
        }

        Row {
            GearsImageDot(
                variant = GearsImageDotVariant.AlignmentTopCenter(painterResource(id = R.drawable.ic_notification))
            )

            GearsImageDot(
                variant = GearsImageDotVariant.AlignmentTopCenter(painterResource(id = R.drawable.ic_notification)),
                count = 99
            )
        }

        Row {
            GearsImageDot(
                variant = GearsImageDotVariant.AlignmentTopEnd(painterResource(id = R.drawable.ic_notification))
            )

            GearsImageDot(
                variant = GearsImageDotVariant.AlignmentTopEnd(painterResource(id = R.drawable.ic_notification)),
                count = 99
            )
        }


        Row {
            GearsImageDot(
                variant = GearsImageDotVariant.AlignmentBottomStart(painterResource(id = R.drawable.ic_notification))
            )

            GearsImageDot(
                variant = GearsImageDotVariant.AlignmentBottomStart(painterResource(id = R.drawable.ic_notification)),
                count = 99
            )
        }

        Row {
            GearsImageDot(
                variant = GearsImageDotVariant.AlignmentBottomCenter(painterResource(id = R.drawable.ic_notification))
            )

            GearsImageDot(
                variant = GearsImageDotVariant.AlignmentBottomCenter(painterResource(id = R.drawable.ic_notification)),
                count = 99
            )
        }

        Row {
            GearsImageDot(
                variant = GearsImageDotVariant.AlignmentBottomEnd(painterResource(id = R.drawable.ic_notification))
            )

            GearsImageDot(
                variant = GearsImageDotVariant.AlignmentBottomEnd(painterResource(id = R.drawable.ic_notification)),
                count = 99
            )
        }
    }
}