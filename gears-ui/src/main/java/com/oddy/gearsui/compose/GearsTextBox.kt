package com.oddy.gearsui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oddy.gearsui.R
import com.oddy.gearsui.utils.disableRipple
import kotlinx.coroutines.flow.collect

@Composable
fun GearsTextBox(
    modifier: Modifier = Modifier,
    prefixIcon: Painter? = null,
    suffixIcon: Painter? = null,
    prefixText: String? = null,
    suffixText: String? = null,
    placeholder: String? = null,
    label: String? = null,
    isOptional: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    helper: String? = null,
    hasClearAction: Boolean = false,
    isEnabled: Boolean = true,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    contentAlignment: Alignment = Alignment.CenterStart,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textFieldValue: TextFieldValue? = null,
    enableSuffixClickToFocusToTextField: Boolean = false,
    onTextFieldValueChanged: ((TextFieldValue) -> Unit)? = null,
    value: String? = null,
    onValueChanged: ((String) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = FocusRequester()
    var rowModifier = if (!isEnabled) {
        Modifier
            .background(
                color = colorResource(id = R.color.monochrome_400),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.monochrome_400),
                shape = RoundedCornerShape(16.dp)
            )
    } else if (isError || errorMessage != null) {
        Modifier
            .background(
                color = colorResource(id = R.color.white),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.ui_red),
                shape = RoundedCornerShape(16.dp)
            )
    } else if (isFocused) {
        Modifier
            .background(
                color = colorResource(id = R.color.white),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.turqoise),
                shape = RoundedCornerShape(16.dp)
            )
    } else {
        Modifier
            .background(
                color = colorResource(id = R.color.white),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.monochrome_300),
                shape = RoundedCornerShape(16.dp)
            )
    }

    val suffixPrefixModifier =
        if (onClick == null) Modifier
        else Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick,
        )

    val suffixPrefixColor = when {
        isError || errorMessage != null -> colorResource(id = R.color.ui_red)
        isFocused -> colorResource(id = R.color.teal)
        else -> colorResource(id = R.color.monochrome_600)
    }

    val textColor = when {
        isError || errorMessage != null -> colorResource(id = R.color.ui_red)
        else -> colorResource(id = R.color.monochrome_800)
    }

    val placeHolderColor = when {
        isEnabled -> colorResource(id = R.color.monochrome_400)
        isError || errorMessage != null -> colorResource(id = R.color.ui_red)
        else -> colorResource(id = R.color.white)
    }

    Column(
        modifier = modifier.onFocusChanged {
            isFocused = it.isFocused || it.hasFocus
            if (it.isFocused) focusRequester.requestFocus()
        }
    ) {
        if (label != null) {
            val labelRowModifier =
                if (helper == null) Modifier.padding(bottom = 5.dp)
                else Modifier
            Row(
                modifier = labelRowModifier
            ) {
                GearsText(
                    text = label,
                    type = GearsTextType.Heading14,
                    textColor = colorResource(id = R.color.monochrome_600)
                )
                if (isOptional) {
                    GearsText(
                        text = stringResource(R.string.optional),
                        type = GearsTextType.Body14,
                        fontStyle = FontStyle.Italic,
                        textColor = colorResource(id = R.color.monochrome_600),
                        modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
                    )
                }
            }
        }

        if (helper != null || errorMessage != null) {
            val helperColor = when {
                isError || errorMessage != null -> colorResource(id = R.color.ui_red)
                else -> colorResource(id = R.color.monochrome_600)
            }

            GearsText(
                text = errorMessage ?: helper.orEmpty(),
                type = GearsTextType.Body14,
                textColor = helperColor,
                modifier = Modifier.padding(bottom = 5.dp)
            )
        }

        rowModifier = if (prefixIcon == null
            && prefixText == null
            && suffixIcon == null
            && suffixText == null
        ) {
            rowModifier.padding(horizontal = 5.dp, vertical = 16.dp)
        } else {
            rowModifier.padding(5.dp)
        }

        Row(
            modifier = rowModifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (prefixIcon != null || prefixText != null) {
                Box(
                    modifier = suffixPrefixModifier
                        .height(40.dp)
                        .background(
                            color = colorResource(id = R.color.monochrome_200),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (prefixIcon != null) {
                        Icon(
                            painter = prefixIcon,
                            contentDescription = "",
                            tint = suffixPrefixColor,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(24.dp)
                        )
                    }

                    if (prefixText != null) {
                        GearsText(
                            text = prefixText,
                            type = GearsTextType.Heading14,
                            textColor = suffixPrefixColor,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                }
            }

            var textStyle = LocalTextStyle.current.copy(
                color = textColor,
                fontSize = 14.sp
            )

            if (contentAlignment == Alignment.Center) textStyle = textStyle.copy(
                textAlign = TextAlign.Center
            )

            val textFieldModifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f)
                .focusRequester(focusRequester)

            if (textFieldValue != null && onTextFieldValueChanged != null) {
                BasicTextField(
                    modifier = textFieldModifier,
                    value = textFieldValue,
                    enabled = isEnabled,
                    onValueChange = onTextFieldValueChanged,
                    visualTransformation = visualTransformation,
                    singleLine = singleLine,
                    textStyle = textStyle,
                    keyboardOptions = keyboardOptions,
                    readOnly = readOnly,
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            if (onClick == null) return@also
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        onClick()
                                    }
                                }
                            }
                        },
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = contentAlignment
                        ) {
                            if (textFieldValue.text.isEmpty() && placeholder != null) GearsText(
                                text = placeholder,
                                type = GearsTextType.Body14,
                                textColor = placeHolderColor
                            )

                            innerTextField()
                        }
                    }
                )
            } else if (value != null && onValueChanged != null) {
                BasicTextField(
                    modifier = textFieldModifier,
                    value = value,
                    enabled = isEnabled,
                    onValueChange = onValueChanged,
                    visualTransformation = visualTransformation,
                    singleLine = singleLine,
                    textStyle = textStyle,
                    keyboardOptions = keyboardOptions,
                    readOnly = readOnly,
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            if (onClick == null) return@also
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        onClick()
                                    }
                                }
                            }
                        },
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = contentAlignment
                        ) {
                            if (value.isEmpty() && placeholder != null) GearsText(
                                text = placeholder,
                                type = GearsTextType.Body14,
                                textColor = placeHolderColor
                            )

                            innerTextField()
                        }
                    }
                )
            }

            if (hasClearAction
                && (!value.isNullOrEmpty() || !textFieldValue?.text.isNullOrEmpty())
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_text_field_clear),
                    contentDescription = "Clear",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            if (onValueChanged != null) onValueChanged("")
                            else if (onTextFieldValueChanged != null) {
                                onTextFieldValueChanged(TextFieldValue())
                            }
                        },
                    tint = Color.Unspecified,
                )
            }

            if (suffixIcon != null || suffixText != null) {
                Box(
                    modifier = suffixPrefixModifier
                        .height(40.dp)
                        .background(
                            color = colorResource(id = R.color.monochrome_200),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .disableRipple(remember { MutableInteractionSource() }) { if (enableSuffixClickToFocusToTextField) focusRequester.requestFocus() },
                    contentAlignment = Alignment.Center
                ) {
                    if (suffixIcon != null) {
                        Icon(
                            painter = suffixIcon,
                            contentDescription = "",
                            tint = suffixPrefixColor,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(24.dp)
                        )
                    }

                    if (suffixText != null) {
                        GearsText(
                            text = suffixText,
                            type = GearsTextType.Heading14,
                            textColor = suffixPrefixColor,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun GearsTextBoxPreviewEmpty() {
    Column(Modifier.background(colorResource(id = R.color.monochrome_200))) {
        val modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()

        GearsTextBox(modifier = modifier, placeholder = "Lorem ipsum dolor sit amet") {}
        GearsTextBox(
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            prefixText = "KM"
        ) {}
        GearsTextBox(
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            prefixIcon = painterResource(id = R.drawable.ic_arrow_left)
        ) {}
        GearsTextBox(
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            suffixText = "KM"
        ) {}
        GearsTextBox(
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            suffixIcon = painterResource(id = R.drawable.ic_arrow_left)
        ) {}
        GearsTextBox(
            modifier = modifier,
            singleLine = false,
            placeholder = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce malesuada sapien id dolor imperdiet feugiat. Aenean nec convallis est. Ut vitae tincidunt quam, in sollicitudin libero.",
        ) {}
    }
}

@Preview
@Composable
fun GearsTextBoxPreviewFilled() {
    Column(Modifier.background(colorResource(id = R.color.monochrome_200))) {
        val modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()

        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet"
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            prefixText = "KM"
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            prefixIcon = painterResource(id = R.drawable.ic_arrow_left)
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            suffixText = "KM"
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            suffixIcon = painterResource(id = R.drawable.ic_arrow_left)
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce malesuada sapien id dolor imperdiet feugiat. Aenean nec convallis est. Ut vitae tincidunt quam, in sollicitudin libero.",
            modifier = modifier,
            singleLine = false,
            placeholder = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce malesuada sapien id dolor imperdiet feugiat. Aenean nec convallis est. Ut vitae tincidunt quam, in sollicitudin libero.",
        ) {}
    }
}

@Preview
@Composable
fun GearsTextBoxPreviewDisabled() {
    Column(Modifier.background(colorResource(id = R.color.monochrome_200))) {
        val modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()

        GearsTextBox(
            isEnabled = false,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet"
        ) {}
        GearsTextBox(
            isEnabled = false,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            prefixText = "KM"
        ) {}
        GearsTextBox(
            isEnabled = false,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            prefixIcon = painterResource(id = R.drawable.ic_arrow_left)
        ) {}
        GearsTextBox(
            isEnabled = false,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            suffixText = "KM"
        ) {}
        GearsTextBox(
            isEnabled = false,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            suffixIcon = painterResource(id = R.drawable.ic_arrow_left)
        ) {}
        GearsTextBox(
            isEnabled = false,
            modifier = modifier,
            singleLine = false,
            placeholder = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce malesuada sapien id dolor imperdiet feugiat. Aenean nec convallis est. Ut vitae tincidunt quam, in sollicitudin libero.",
        ) {}
    }
}

@Preview
@Composable
fun GearsTextBoxPreviewError() {
    Column(Modifier.background(colorResource(id = R.color.monochrome_200))) {
        val modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()

        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            isError = true,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet"
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            isError = true,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            prefixText = "KM"
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            isError = true,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            prefixIcon = painterResource(id = R.drawable.ic_arrow_left)
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            isError = true,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            suffixText = "KM"
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            isError = true,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            suffixIcon = painterResource(id = R.drawable.ic_arrow_left)
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce malesuada sapien id dolor imperdiet feugiat. Aenean nec convallis est.",
            isError = true,
            modifier = modifier,
            singleLine = false,
            placeholder = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce malesuada sapien id dolor imperdiet feugiat. Aenean nec convallis est. Ut vitae tincidunt quam, in sollicitudin libero.",
        ) {}
    }
}

@Preview
@Composable
fun GearsTextBoxPreviewHasClearAction() {
    Column(Modifier.background(colorResource(id = R.color.monochrome_200))) {
        val modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()

        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            hasClearAction = true,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet"
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            hasClearAction = true,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            prefixText = "KM"
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            hasClearAction = true,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            prefixIcon = painterResource(id = R.drawable.ic_arrow_left)
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            hasClearAction = true,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            suffixText = "KM"
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet",
            hasClearAction = true,
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            suffixIcon = painterResource(id = R.drawable.ic_arrow_left)
        ) {}
        GearsTextBox(
            value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce malesuada sapien id dolor imperdiet feugiat. Aenean nec convallis est.",
            hasClearAction = true,
            modifier = modifier,
            singleLine = false,
            placeholder = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce malesuada sapien id dolor imperdiet feugiat. Aenean nec convallis est. Ut vitae tincidunt quam, in sollicitudin libero.",
        ) {}
    }
}

@Composable
@Preview
private fun GearsTextBoxWithLabel() {
    Column(Modifier.background(colorResource(id = R.color.monochrome_200))) {
        val modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()

        GearsTextBox(
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            label = "Label"
        ) {}

        GearsTextBox(
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            label = "Label",
            helper = "Helper text"
        ) {}

        GearsTextBox(
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            label = "Label",
            isOptional = true,
        ) {}

        GearsTextBox(
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            label = "Label",
            isOptional = true,
            isEnabled = false,
            helper = "Helper text"
        ) {}

        GearsTextBox(
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            label = "Label",
            isOptional = true,
            hasClearAction = true,
            helper = "Helper text"
        ) {}

        GearsTextBox(
            modifier = modifier,
            placeholder = "Lorem ipsum dolor sit amet",
            label = "Label",
            isOptional = true,
            errorMessage = "Error mas",
            helper = "Helper text"
        ) {}
    }
}
