package com.oddy.gearsui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.oddy.gearsui.R

sealed class GearsButtonSize(
    val verticalMargin: Dp,
    val horizontalMargin: Dp,
) {
    object Normal : GearsButtonSize(12.dp, 30.dp)
    object Small : GearsButtonSize(7.dp, 30.dp)
}

@Composable
fun GearsButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    color: Color = colorResource(id = R.color.teal),
    textColor: Color = colorResource(id = R.color.white),
    size: GearsButtonSize = GearsButtonSize.Normal,
    isFilled: Boolean = true,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    isError: Boolean = false
) {
    val backgroundColor = when {
        !isEnabled -> colorResource(id = R.color.black_500)
        isError -> colorResource(R.color.ui_red)
        isLoading -> colorResource(id = R.color.black_500)
        else -> color
    }

    if (isFilled) {
        Button(
            onClick = onClick,
            enabled = isEnabled && !isLoading,
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = backgroundColor),
            contentPadding = PaddingValues(
                vertical = size.verticalMargin,
                horizontal = size.horizontalMargin
            )
        ) {
            GearsButtonContent(text, isLoading, isFilled, backgroundColor, textColor)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            enabled = isEnabled && !isLoading,
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
            border = BorderStroke(2.dp, backgroundColor),
            contentPadding = PaddingValues(
                vertical = size.verticalMargin,
                horizontal = size.horizontalMargin
            ),
        ) {
            GearsButtonContent(text, isLoading, isFilled, backgroundColor, textColor)
        }
    }
}

@Composable
private fun GearsButtonContent(
    text: String,
    isLoading: Boolean,
    isFilled: Boolean,
    backgroundColor: Color,
    textColor: Color,
) {
    if (isLoading) CircularProgressIndicator(color = colorResource(id = R.color.black_900))
    else GearsText(
        text = text,
        type = GearsTextType.Heading16,
        textColor = if (!isFilled) backgroundColor else textColor
    )
}

@Preview
@Composable
private fun GearsButtonPreview() {
    val onClick: () -> Unit = {}
    Row(Modifier.background(colorResource(id = R.color.grey_100))) {
        Column {
            GearsButton(onClick = onClick, text = "Text")
            GearsButton(onClick = onClick, text = "Text", isFilled = false)
            GearsButton(onClick = onClick, text = "Text", isEnabled = false)
            GearsButton(onClick = onClick, text = "Text", isError = true)
            GearsButton(onClick = onClick, text = "Text", isLoading = true)
        }

        Column {
            GearsButton(onClick = onClick, text = "Text", size = GearsButtonSize.Small)
            GearsButton(
                onClick = onClick,
                text = "Text",
                size = GearsButtonSize.Small,
                isFilled = false
            )
            GearsButton(
                onClick = onClick,
                text = "Text",
                size = GearsButtonSize.Small,
                isEnabled = false
            )
            GearsButton(
                onClick = onClick,
                text = "Text",
                size = GearsButtonSize.Small,
                isError = true
            )
            GearsButton(
                onClick = onClick,
                text = "Text",
                size = GearsButtonSize.Small,
                isLoading = true
            )
        }
    }
}