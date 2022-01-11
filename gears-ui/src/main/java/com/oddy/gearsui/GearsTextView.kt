package com.oddy.gearsui

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnPreDraw

class GearsTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {
    var size: Int = 0
        set(value) {
            field = value
            when (value) {
                HIGH_SPEED_30 -> setTextSizeAndLineHeight(30, R.dimen.high_speed_30_line_height)
                HIGH_SPEED_24 -> setTextSizeAndLineHeight(24, R.dimen.high_speed_24_line_height)
                HIGH_SPEED_20 -> setTextSizeAndLineHeight(20, R.dimen.high_speed_20_line_height)
                HIGH_SPEED_18 -> setTextSizeAndLineHeight(18, R.dimen.high_speed_18_line_height)
                HIGH_SPEED_16 -> setTextSizeAndLineHeight(16, R.dimen.high_speed_16_line_height)
                HIGH_SPEED_14 -> setTextSizeAndLineHeight(14, R.dimen.high_speed_14_line_height)
                LOW_SPEED_16 -> setTextSizeAndLineHeight(16, R.dimen.low_speed_16_line_height)
                LOW_SPEED_14 -> setTextSizeAndLineHeight(14, R.dimen.low_speed_14_line_height)
                LOW_SPEED_12 -> setTextSizeAndLineHeight(12, R.dimen.low_speed_12_line_height)
            }
        }

    private fun setTextSizeAndLineHeight(newTextSize: Int, @DimenRes lineHeightRes: Int) {
        textSize = newTextSize.toFloat()
        lineHeight = context.resources.getDimensionPixelSize(lineHeightRes)
    }

    init {
        val defaultColor = ContextCompat.getColor(context, R.color.black_900)
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GearsTextView,
            0,
            0
        ).apply {
            try {
                size = getInteger(R.styleable.GearsTextView_size, 0)
                val gearsTextColor =
                    getColor(R.styleable.GearsTextView_android_textColor, defaultColor)
                setTextColor(gearsTextColor)
            } finally {
                recycle()
            }
        }
        letterSpacing = 0f

        if (!isInEditMode) {
            if (size <= 5) setTypeface(Companion.getTypeface(context), Typeface.BOLD)
            else setTypeface(Companion.getTypeface(context), Typeface.NORMAL)
        } else {
            if (size <= 5) setTypeface(typeface, Typeface.BOLD)
            else setTypeface(typeface, Typeface.NORMAL)
        }
    }

    private fun createSpan(originalText: String, spanText: String, listener: (() -> Unit)?) = run {
        val startIndex = originalText.indexOf(spanText)
        SpannableString(originalText).apply {
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        listener?.invoke()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.apply {
                            color = ContextCompat.getColor(context, R.color.teal)
                            isUnderlineText = false
                            typeface = Typeface.create(getTypeface(context), Typeface.BOLD)
                        }
                    }
                },
                startIndex,
                startIndex + spanText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    companion object {
        private var typefaceInstance: Typeface? = null
        fun getTypeface(context: Context) = typefaceInstance ?: run {
            typefaceInstance = ResourcesCompat.getFont(context, R.font.effra)
            typefaceInstance
        }

        const val HIGH_SPEED_30 = 0
        const val HIGH_SPEED_24 = 1
        const val HIGH_SPEED_20 = 2
        const val HIGH_SPEED_18 = 3
        const val HIGH_SPEED_16 = 4
        const val HIGH_SPEED_14 = 5
        const val LOW_SPEED_16 = 6
        const val LOW_SPEED_14 = 7
        const val LOW_SPEED_12 = 8
    }
}