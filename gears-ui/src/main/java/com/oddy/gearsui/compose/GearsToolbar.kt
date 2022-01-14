package com.oddy.gearsui.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oddy.gearsui.R

sealed class GearsToolbarVariant {
    var extraDrawable: ExtraDrawable? = null

    sealed class ExtraDrawable {
        object NoDrawableEnd : ExtraDrawable()
        data class DrawableEnd(@DrawableRes val drawableEnd: Int) : ExtraDrawable()
        data class DrawableEndWithDot(val gearsImageDotVariant: GearsImageDotVariant) :
            ExtraDrawable()
    }

    data class VariantLight(
        @DrawableRes val vDrawableEnd: Int? = null,
        val vGearsImageDotVariantEnd: GearsImageDotVariant? = null
    ) : GearsToolbarVariant() {
        init {
            extraDrawable = when {
                vDrawableEnd == null && vGearsImageDotVariantEnd == null -> ExtraDrawable.NoDrawableEnd
                vGearsImageDotVariantEnd != null -> ExtraDrawable.DrawableEndWithDot(
                    vGearsImageDotVariantEnd
                )
                vDrawableEnd != null -> ExtraDrawable.DrawableEnd(vDrawableEnd)
                else -> ExtraDrawable.NoDrawableEnd
            }
        }
    }

    data class VariantDark(
        @DrawableRes val vDrawableEnd: Int? = null,
        val vGearsImageDotVariantEnd: GearsImageDotVariant? = null
    ) : GearsToolbarVariant() {
        init {
            extraDrawable = when {
                vDrawableEnd == null && vGearsImageDotVariantEnd == null -> ExtraDrawable.NoDrawableEnd
                vGearsImageDotVariantEnd != null -> ExtraDrawable.DrawableEndWithDot(
                    vGearsImageDotVariantEnd
                )
                vDrawableEnd != null -> ExtraDrawable.DrawableEnd(vDrawableEnd)
                else -> ExtraDrawable.NoDrawableEnd
            }
        }
    }
}

@Composable
fun GearsToolbar(
    modifier: Modifier = Modifier,
    variant: GearsToolbarVariant,
    title: String? = null,
    count: Int? = null
) {
    val backgroundColor =
        if (variant is GearsToolbarVariant.VariantDark) colorResource(id = R.color.black_900)
        else colorResource(id = R.color.white)

    val tintColor =
        if (variant is GearsToolbarVariant.VariantDark)
            colorResource(id = R.color.white)
        else colorResource(id = R.color.black_900)

    val mModifier = modifier
        .background(
            color = backgroundColor,
            shape = if (variant is GearsToolbarVariant.VariantDark) RoundedCornerShape(
                bottomEnd = 32.dp,
                bottomStart = 32.dp
            ) else RectangleShape
        )
        .fillMaxWidth()
        .height(72.dp)

    Box(
        modifier = mModifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        Icon(
            modifier = Modifier.align(Alignment.CenterStart),
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = "arrow_left",
            tint = tintColor
        )

        if (variant is GearsToolbarVariant.VariantLight) {
            GearsText(
                modifier = Modifier.align(Alignment.Center),
                text = title.orEmpty(),
                type = GearsTextType.Heading18,
                textColor = colorResource(id = R.color.monochrome_800)
            )
        } else if (variant is GearsToolbarVariant.VariantDark) {
            GearsText(
                modifier = Modifier.align(Alignment.Center),
                text = title.orEmpty(),
                type = GearsTextType.Heading18,
                textColor = colorResource(id = R.color.white)
            )
        }

        when (variant.extraDrawable) {
            is GearsToolbarVariant.ExtraDrawable.DrawableEnd -> {
                val painter =
                    painterResource(id = (variant.extraDrawable as GearsToolbarVariant.ExtraDrawable.DrawableEnd).drawableEnd)
                Image(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    painter = painter,
                    contentDescription = "drawable_end"
                )
            }
            is GearsToolbarVariant.ExtraDrawable.DrawableEndWithDot -> {
                GearsImageDot(
                    variant = (variant.extraDrawable as GearsToolbarVariant.ExtraDrawable.DrawableEndWithDot).gearsImageDotVariant,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    count = count
                )
            }
            is GearsToolbarVariant.ExtraDrawable.NoDrawableEnd -> Unit
            else -> Unit
        }
    }
}

@Preview
@Composable
fun PreviewGearsToolbar() {
    Column(modifier = Modifier.background(Color.Gray)) {
        GearsToolbar(
            modifier = Modifier.padding(vertical = 5.dp),
            variant = GearsToolbarVariant.VariantDark(R.drawable.ic_notification),
            title = "List kendaraan"
        )

        GearsToolbar(
            modifier = Modifier.padding(vertical = 5.dp),
            variant = GearsToolbarVariant.VariantDark(
                vGearsImageDotVariantEnd = GearsImageDotVariant.AlignmentTopEnd(
                    painterResource(id = R.drawable.ic_notification)
                )
            ),
            title = "List kendaraan",
            count = 10
        )

        GearsToolbar(
            modifier = Modifier.padding(vertical = 5.dp),
            variant = GearsToolbarVariant.VariantLight(R.drawable.ic_notification),
            title = "List kendaraan"
        )

        GearsToolbar(
            modifier = Modifier.padding(vertical = 5.dp),
            variant = GearsToolbarVariant.VariantLight(
                vGearsImageDotVariantEnd = GearsImageDotVariant.AlignmentTopEnd(
                    painterResource(id = R.drawable.ic_notification)
                )
            ),
            title = "List kendaraan",
            count = 30
        )

        GearsToolbar(
            modifier = Modifier.padding(vertical = 5.dp),
            variant = GearsToolbarVariant.VariantLight(),
            title = "List kendaraan"
        )

        GearsToolbar(
            modifier = Modifier.padding(vertical = 5.dp),
            variant = GearsToolbarVariant.VariantDark(),
            title = "List kendaraan"
        )

        GearsToolbar(
            modifier = Modifier.padding(vertical = 5.dp),
            variant = GearsToolbarVariant.VariantLight()
        )

        GearsToolbar(
            modifier = Modifier.padding(vertical = 5.dp),
            variant = GearsToolbarVariant.VariantDark()
        )
    }
}





