package com.oddy.gearsui

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged

class GearsOtpField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        gravity = Gravity.CENTER
        initAttributes(context, attrs)
    }

    private var fieldNum: Int = 0
    private var margin: Int = 0

    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GearsOtpField,
            0,
            0
        ).apply {
            try {
                fieldNum = getInt(R.styleable.GearsOtpField_gof_fieldnum, 1)
                margin = getDimensionPixelSize(R.styleable.GearsOtpField_gof_margin, 0)
                changeFieldSize(fieldNum, margin)
            } finally {
                recycle()
            }
        }
    }

    fun changeFieldSize(fieldNum: Int, marginPixel: Int) {
        this.fieldNum = fieldNum
        this.margin = marginPixel
        buildItems()
    }

    private fun buildItems() {
        removeAllViews()
        for (i in 0 until fieldNum) {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.layout_gears_otp_field_item, this, false) as GearsOtpEditText
            view.doOnTextChanged { _, _, before, count ->
                onTextChanged(before, count, i)
            }
            view.setOnKeyListener { _, _, event ->
                if (onKeyBackwardCheck(event, i)) {
                    return@setOnKeyListener true
                }
                if (onKeyForwardCheck(event, i)) {
                    return@setOnKeyListener true
                }
                if (event.action == KeyEvent.ACTION_UP) {
                    return@setOnKeyListener onKeyActionUp(event, i)
                }
                false
            }
            addView(view)
            view.layoutParams = (view.layoutParams as LayoutParams).apply {
                if (i > 0) setMargins(margin, 0, 0, 0)
            }
        }
    }

    private fun onTextChanged(before: Int, count: Int, pos: Int) {
        if (before != count) {
            if (count == 1 && !checkCompletion() && pos < childCount - 1) {
                getChildAt(pos + 1)?.requestFocus()
            }
        }
    }

    private fun onKeyBackwardCheck(event: KeyEvent, pos: Int): Boolean {
        val editText = getChildAt(pos) as AppCompatEditText
        if (
            editText.text?.length == 0
            && pos > 0
            && event.keyCode == KeyEvent.KEYCODE_DEL
        ) {
            getChildAt(pos - 1).dispatchKeyEvent(event)
            if (event.action == KeyEvent.ACTION_DOWN) getChildAt(pos - 1).requestFocus()
            return true
        }
        return false
    }

    private fun onKeyForwardCheck(event: KeyEvent, pos: Int): Boolean {
        val editText = getChildAt(pos) as AppCompatEditText
        if (
            editText.text?.length == 1
            && pos < fieldNum - 1
            && event.keyCode != KeyEvent.KEYCODE_DEL
            && event.keyCode != KeyEvent.KEYCODE_ENTER
        ) {
            getChildAt(pos + 1).dispatchKeyEvent(event)
            return true
        }
        return false
    }

    private fun onKeyActionUp(event: KeyEvent, pos: Int): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
            checkCompletion()
            return true
        }
        val editText = getChildAt(pos) as AppCompatEditText
        if (pos > 0 && editText.text?.length == 0 && event.keyCode == KeyEvent.KEYCODE_DEL) {
            getChildAt(pos - 1).requestFocus()
            return true
        }
        if (pos == fieldNum - 1) {
            return true
        }
        return false
    }

    private fun checkCompletion(): Boolean {
        val text = StringBuilder()
        children.forEach {
            text.append((it as AppCompatEditText).text)
        }
        if (text.length == fieldNum) {
            _otpCompletionListener?.invoke(text.toString())
            return true
        }
        return false
    }

    fun requestOtpFocus() {
        getChildAt(0)?.requestFocus()
    }

    private var _otpCompletionListener: ((String) -> Unit)? = null
    fun setOtpCompletionListener(listener: (String) -> Unit) {
        _otpCompletionListener = listener
    }

    companion object {
        private var _startGeneratedId: Int? = null
        val startGeneratedId: Int
            get() {
                if (_startGeneratedId == null) _startGeneratedId = View.generateViewId()
                return _startGeneratedId!!
            }
    }
}