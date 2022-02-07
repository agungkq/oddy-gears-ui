package com.oddy.gearsui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class GearsOtpEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {
    init {
        setTextColor(ContextCompat.getColor(context, R.color.black_900))
    }

    override fun onSelectionChanged(start: Int, end: Int) {
        if (text != null) {
            if (start != text!!.length || end != text!!.length) {
                setSelection(text!!.length, text!!.length)
                return
            }
        }
        super.onSelectionChanged(start, end)
    }
}