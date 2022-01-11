package com.oddy.gearsui.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oddy.gearsui.R
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun HeaderMain(
    avatar: String?,
    avatarIndicatorCount: Int? = null,
    avatarOnClick: () -> Unit = {},
    @DrawableRes drawableEnd: Int? = null,
    drawableEndIndicatorCount: Int? = null,
    drawableEndContentDescription: String? = null,
    drawableEndOnClick: () -> Unit = {},
    rounded: Boolean = true
) {
    Row(
        modifier = Modifier
            .background(
                color = colorResource(id = R.color.black_900),
                shape = if (rounded) RoundedCornerShape(
                    bottomEnd = 32.dp,
                    bottomStart = 32.dp
                ) else RectangleShape
            )
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val drawableModifier = Modifier
            .width(40.dp)
            .height(40.dp)
        Box(modifier = drawableModifier.clickable(onClick = avatarOnClick)) {
            CoilImage(
                modifier = drawableModifier.clip(CircleShape),
                imageModel = if (avatar.isNullOrEmpty()) R.drawable.ic_not_logged_in else avatar
            )

            if (avatarIndicatorCount != null) {
                CountIndicator(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    count = avatarIndicatorCount
                )
            }
        }

        Icon(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth()
                .width(95.dp)
                .height(48.dp),
            tint = colorResource(id = R.color.white),
            painter = painterResource(id = R.drawable.ic_brand),
            contentDescription = "Brand Oddy"
        )

        Box(modifier = drawableModifier.clickable(onClick = drawableEndOnClick)) {
            val drawableEndModifier = Modifier
                .width(32.dp)
                .height(32.dp)
            if (drawableEnd != null) {
                Image(
                    modifier = drawableEndModifier.align(Alignment.Center),
                    painter = painterResource(id = drawableEnd),
                    contentDescription = drawableEndContentDescription
                )

                if (drawableEndIndicatorCount != null) {
                    CountIndicator(
                        modifier = Modifier.align(Alignment.TopEnd),
                        count = drawableEndIndicatorCount
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun HeaderMainPreview() {
    MaterialTheme {
        Column {
            HeaderMain(avatar = null, rounded = false)
            HeaderMain(avatar = null)
            HeaderMain(avatar = null, drawableEnd = R.drawable.ic_notification)
            HeaderMain(
                avatar = null,
                avatarIndicatorCount = 5,
                drawableEnd = R.drawable.ic_notification,
                drawableEndIndicatorCount = 10
            )
        }
    }
}