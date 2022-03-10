package com.oddy.gearsui.compose

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.oddy.gearsui.R

@Composable
fun GearsPackageContainer(
    modifier: Modifier = Modifier,
    packageName: String,
    totalPackagePriceText: String,
    totalItemsText: String,
    onExpandClicked: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {

    var shouldContentVisible by remember { mutableStateOf(false) }
    val bodyModifier = modifier
        .fillMaxWidth()
        .background(
            color = colorResource(id = R.color.monochrome_100),
            shape = RoundedCornerShape(16.dp)
        )
        .padding(top = 15.dp, start = 20.dp, end = 20.dp)

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.monochrome_100),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        val (body, lExpandCollapsed) = createRefs()

        ConstraintLayout(modifier = bodyModifier.constrainAs(body) {}) {
            val (gtvPackageName, imgMaintenance, dotted, gtvTotalPackagePrice, gtvTotalItems, lContent, lSubName) = createRefs()
            GearsText(
                modifier = Modifier.constrainAs(gtvPackageName) {
                    start.linkTo(parent.start)
                    end.linkTo(imgMaintenance.start)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                },
                textAlign = TextAlign.Left,
                text = packageName,
                type = GearsTextType.Heading18,
                textColor = colorResource(id = R.color.monochrome_800)
            )

            Image(
                modifier = Modifier.constrainAs(imgMaintenance) {
                    top.linkTo(gtvPackageName.top)
                    bottom.linkTo(gtvPackageName.bottom)
                    end.linkTo(parent.end)
                }, painter = painterResource(
                    id =
                    R.drawable.ic_maintenance
                ), contentDescription = "img_maintenance"
            )

            AnimatedVisibility(
                modifier = Modifier.constrainAs(lSubName) {
                    top.linkTo(gtvPackageName.bottom, margin = 2.dp)
                    start.linkTo(parent.start)
                },
                visible = !shouldContentVisible,
                enter = fadeIn(animationSpec = tween(2000)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                ConstraintLayout {
                    GearsText(
                        modifier = Modifier.constrainAs(gtvTotalPackagePrice) {
                            top.linkTo(gtvPackageName.bottom, margin = 2.dp)
                            start.linkTo(parent.start)
                        },
                        text = totalPackagePriceText,
                        textColor = colorResource(id = R.color.dark_cyan),
                        type = GearsTextType.Heading16
                    )

                    Box(modifier = Modifier
                        .constrainAs(dotted) {
                            top.linkTo(gtvTotalPackagePrice.top)
                            bottom.linkTo(gtvTotalPackagePrice.bottom)
                            start.linkTo(gtvTotalPackagePrice.end, margin = 5.dp)
                        }
                        .size(3.dp)
                        .background(color = colorResource(id = R.color.monochrome_800)))

                    GearsText(
                        modifier = Modifier.constrainAs(gtvTotalItems) {
                            start.linkTo(dotted.end, margin = 5.dp)
                            top.linkTo(gtvTotalPackagePrice.top)
                            bottom.linkTo(gtvTotalPackagePrice.bottom)
                        },
                        text = totalItemsText,
                        type = GearsTextType.Body14,
                        textColor = colorResource(id = R.color.monochrome_600)
                    )
                }
            }

            Box(modifier = Modifier
                .constrainAs(lContent) {
                    top.linkTo(
                        if (shouldContentVisible) gtvPackageName.bottom else lSubName.top,
                        margin = 15.dp
                    )
                }
                .fillMaxWidth()) {
                AnimatedVisibility(
                    visible = shouldContentVisible,
                    enter = fadeIn(animationSpec = tween(1000)) + expandVertically(
                        animationSpec = tween(1500)
                    ),
                    exit = fadeOut(animationSpec = tween(1000)) +
                            shrinkVertically(
                                animationSpec = tween(1500)
                            )
                ) {
                    content()
                }
            }
        }

        Box(modifier = Modifier
            .constrainAs(lExpandCollapsed) {
                top.linkTo(body.bottom, margin = 16.dp)
            }
            .background(
                color = colorResource(id = R.color.monochrome_200),
                shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
            )
            .fillMaxWidth()
            .clickable {
                if (onExpandClicked == null)
                    shouldContentVisible = !shouldContentVisible
                else onExpandClicked.invoke()
            }) {
            Image(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .align(Alignment.Center)
                    .rotate(if (shouldContentVisible) 180f else 0f),
                painter = painterResource(id = R.drawable.ic_chevron_down),
                contentDescription = "ic_chevron"
            )
        }
    }
}

@Preview
@Composable
fun PreviewGearsPackageContainer() {
    GearsPackageContainer(
        packageName = "Servis 10.000 KM",
        totalPackagePriceText = "Rp111.000",
        totalItemsText = "7 item servis",
        onExpandClicked = null,
    ) {
        Column() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5
                ) {}

                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5,
                    shape = RoundedCornerShape(16.dp)
                ) {}
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5
                ) {}

                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5,
                    shape = RoundedCornerShape(16.dp)
                ) {}
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5
                ) {}

                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5,
                    shape = RoundedCornerShape(16.dp)
                ) {}
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5
                ) {}

                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5,
                    shape = RoundedCornerShape(16.dp)
                ) {}
            }
        }
    }
}

@Preview
@Composable
fun PreviewGearsPackageContainer2() {
    GearsPackageContainer(
        packageName = "Servis 10.000 KM",
        totalPackagePriceText = "Rp111.000",
        totalItemsText = "7 item servis",
        onExpandClicked = { },
    ) {
        Column() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5
                ) {}

                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5,
                    shape = RoundedCornerShape(16.dp)
                ) {}
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5
                ) {}

                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5,
                    shape = RoundedCornerShape(16.dp)
                ) {}
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5
                ) {}

                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5,
                    shape = RoundedCornerShape(16.dp)
                ) {}
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5
                ) {}

                GearsBackgroundGradient(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(10.dp),
                    variant = GearsGradientVariant.Variant5,
                    shape = RoundedCornerShape(16.dp)
                ) {}
            }
        }
    }
}