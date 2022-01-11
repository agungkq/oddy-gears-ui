package com.oddy.gearsui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oddy.gearsui.R

@Composable
fun CountIndicator(modifier: Modifier = Modifier, count: Int? = null) {
    val size = if (count == null) 12.dp else 18.dp
    Box(
        modifier = modifier
            .width(size)
            .height(size)
            .background(color = colorResource(R.color.ui_red), shape = CircleShape)
            .border(
                width = 2.dp,
                color = colorResource(id = R.color.black_900),
                shape = CircleShape
            )
    ) {
        if (count != null) {
            GearsText(
                text = count.toString(),
                modifier = Modifier.align(Alignment.Center),
                type = GearsTextType.Body12,
                textColor = colorResource(id = R.color.white),
                textSize = 10.sp
            )
        }
    }
}

@Composable
@Preview
private fun CountIndicatorPreview() {
    Column {
        CountIndicator()
        CountIndicator(count = 5)
    }
}