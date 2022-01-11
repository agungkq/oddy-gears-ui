package com.oddy.gearsui

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat

class GearsButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {
    private var defaultTextColor = 0
    private var hasCustomColor = false

    var buttonSize: Int = 0
        set(value) {
            field = value
            when (value) {
                SMALL -> {
                    setPadding(
                        R.dimen.gears_button_padding_horizontal_small,
                        R.dimen.gears_button_padding_vertical_small
                    )
                    textSize = 14f
                }
                MEDIUM -> {
                    setPadding(
                        R.dimen.gears_button_padding_horizontal_medium,
                        R.dimen.gears_button_padding_vertical_medium
                    )
                    textSize = 16f
                }
                LARGE -> {
                    setPadding(
                        R.dimen.gears_button_padding_horizontal_large,
                        R.dimen.gears_button_padding_vertical_large
                    )
                    textSize = 18f
                }
            }
        }

    var buttonTheme: Int = 0

    var buttonType: Int = 0
        set(value) {
            field = value
            setTextColor(defaultTextColor)
            background = when (value) {
                FILLED -> ContextCompat.getDrawable(context, R.drawable.bg_gears_button_filled)
                OUTLINE -> {
                    if (hasCustomColor) setTextColor(defaultTextColor)
                    else setTextColor(ContextCompat.getColor(context, themedColor))
                    ContextCompat.getDrawable(context, themedBackground)
                }
                ERROR -> ContextCompat.getDrawable(context, R.drawable.bg_gears_button_error)
                else -> null
            }
        }

    private val themedColor
        get() = when (buttonTheme) {
            THEME_RED -> R.color.ui_red
            else -> R.color.teal
        }

    private val themedBackground
        get() = when(buttonType) {
            OUTLINE -> when (buttonTheme) {
                THEME_RED -> R.drawable.bg_gears_button_outline_red
                else -> R.drawable.bg_gears_button_outline
            }
            else -> 0
        }

    private fun setPadding(@DimenRes horizontalRes: Int, @DimenRes verticalRes: Int) {
        context.resources.apply {
            setPadding(
                getDimensionPixelSize(horizontalRes),
                getDimensionPixelSize(verticalRes),
                getDimensionPixelSize(horizontalRes),
                getDimensionPixelSize(verticalRes)
            )
        }
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GearsButton,
            0,
            0
        ).apply {
            try {
                hasCustomColor = hasValue(R.styleable.GearsButton_android_textColor)
                defaultTextColor = getColor(
                    R.styleable.GearsButton_android_textColor,
                    ContextCompat.getColor(context, R.color.white)
                )
                buttonSize = getInteger(R.styleable.GearsButton_gb_buttonSize, 0)
                buttonTheme = getInteger(R.styleable.GearsButton_gb_buttonTheme, 0)
                buttonType = getInteger(R.styleable.GearsButton_gb_buttonType, 0)

                val defaultTextAlignment =
                    getInteger(R.styleable.GearsButton_android_textAlignment, TEXT_ALIGNMENT_CENTER)
                textAlignment = defaultTextAlignment
            } finally {
                recycle()
            }
        }
        setTypeface(GearsTextView.getTypeface(context), Typeface.BOLD)
        letterSpacing = 0f
    }

    companion object {
        const val SMALL = 0
        const val MEDIUM = 1
        const val LARGE = 2

        const val FILLED = 0
        const val OUTLINE = 1
        const val ERROR = 2

        const val THEME_TEAL = 0
        const val THEME_RED = 1
    }
}