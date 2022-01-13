package com.oddy.gearsui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oddy.gearsui.R

sealed class GearsGradientVariant(val listColor: List<Int>) {
    object Variant1 :
        GearsGradientVariant(arrayListOf(R.color.prussian, R.color.dark_cyan, R.color.teal))

    object Variant2 :
        GearsGradientVariant(arrayListOf(R.color.teal, R.color.teal, R.color.grey_blue))

    object Variant3 : GearsGradientVariant(arrayListOf(R.color.lime, R.color.dark_cyan))

    object Variant4 : GearsGradientVariant(arrayListOf(R.color.lime, R.color.teal))

    object Variant5 : GearsGradientVariant(
        arrayListOf(R.color.black_800, R.color.prussian, R.color.dark_cyan)
    )
}

@Composable
fun GearsBackgroundGradient(modifier: Modifier, variant: GearsGradientVariant) {
    val colors = variant.listColor.map { colorResource(id = it) }
    val brush = Brush.verticalGradient(colors = colors)
    Box(modifier = modifier.background(brush = brush))
}

@Preview
@Composable
fun Preview() {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        GearsBackgroundGradient(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth(),
            variant = GearsGradientVariant.Variant1
        )

        GearsBackgroundGradient(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth(),
            variant = GearsGradientVariant.Variant2
        )

        GearsBackgroundGradient(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth(),
            variant = GearsGradientVariant.Variant3
        )

        GearsBackgroundGradient(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth(),
            variant = GearsGradientVariant.Variant4
        )

        GearsBackgroundGradient(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth(),
            variant = GearsGradientVariant.Variant5
        )
    }
}