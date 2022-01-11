package com.oddy.gearsui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.appcompat.widget.AppCompatEditText

class GearsOtpEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {
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