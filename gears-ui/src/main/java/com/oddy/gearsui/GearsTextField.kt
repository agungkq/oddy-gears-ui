package com.oddy.gearsui

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import java.text.NumberFormat
import java.util.*

class GearsTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var isTextFieldEnableSaveState: Boolean = true
        get() = etContent.isSaveEnabled
        set(value) {
            field = value
            etContent.isSaveEnabled = field
        }

    var maxLength: Int = -1
        set(value) {
            if (value == -1) return
            field = value
            etContent.filters = arrayOf(InputFilter.LengthFilter(value))
        }

    var minLine: Int = 1
        set(value) {
            field = value
            etContent.minLines = field
        }

    var maxLine: Int = 1
        set(value) {
            field = value
            etContent.maxLines = field
        }

    var prefixText: String? = null
        set(value) {
            if (value == null) return
            field = value

            flSuffix.visibility = View.GONE
            flPrefix.visibility = View.VISIBLE
            ivPrefix.visibility = View.GONE
            tvPrefix.apply {
                visibility = View.VISIBLE
                text = value
            }
        }

    var prefixIcon: Drawable? = null
        set(value) {
            if (value == null) return
            field = value

            flPrefix.visibility = View.VISIBLE
            tvPrefix.visibility = View.GONE
            ivPrefix.apply {
                visibility = View.VISIBLE
                setImageDrawable(value)
            }
        }

    var suffixIcon: Drawable? = null
        set(value) {
            if (value == null) return
            field = value

            flPrefix.visibility = View.GONE
            flSuffix.visibility = View.VISIBLE
            tvSuffix.visibility = View.GONE
            ivSuffix.apply {
                visibility = View.VISIBLE
                setImageDrawable(value)
            }
        }

    var suffixText: String? = null
        set(value) {
            if (value == null) return
            field = value

            flSuffix.visibility = View.VISIBLE
            flPrefix.visibility = View.GONE
            ivSuffix.visibility = View.GONE
            tvSuffix.apply {
                visibility = View.VISIBLE
                text = value
            }
        }

    var shouldBeEnabled: Boolean = true
        set(value) {
            field = value
            etContent.isEnabled = value

            if (value) reset()
            else disable()
        }

    var hint: String? = null
        set(value) {
            if (value == null) return
            field = value
            etContent.hint = value
        }

    var inputType: Int = 0
        set(value) {
            field = value
            etContent.inputType = value
        }

    var label: String? = null
        set(value) {
            field = value

            if (value.isNullOrEmpty()) {
                tvLabel.visibility = View.GONE
            } else {
                tvLabel.visibility = View.VISIBLE
                tvLabel.text = value
            }
        }

    var optional: Boolean = false
        set(value) {
            field = value
            tvOptional.visibility = if (value) View.VISIBLE else View.GONE
        }

    var helper: String? = null
        set(value) {
            field = value

            if (value.isNullOrEmpty()) {
                tvHelper.visibility = View.GONE
            } else {
                tvHelper.visibility = View.VISIBLE
                tvHelper.text = value
            }
        }

    var labelColor: Int = 0
        set(value) {
            field = value
            tvLabel.setTextColor(value)
        }

    var optionalColor: Int = 0
        set(value) {
            field = value
            tvOptional.setTextColor(value)
        }

    var helperColor: Int = 0
        set(value) {
            field = value
            tvHelper.setTextColor(value)
        }

    val text: String
        get() = etContent.text.toString()

    var error: Boolean = false
        set(value) {
            field = value
            if (value) indicateError()
            else reset()
        }

    var errorMessage: String? = null
        set(value) {
            field = value
            error = value != null
        }

    var enableClear: Boolean = false
        set(value) {
            field = value
            if (!value) {
                etContent.doOnTextChanged { _, _, _, _ -> }
                ivAction.isVisible = false
            } else {
                etContent.doOnTextChanged { text, _, _, _ ->
                    ivAction.isVisible = text?.isNotEmpty() == true
                }
            }
        }

    var imeOptions = EditorInfo.IME_ACTION_UNSPECIFIED
        set(value) {
            field = value
            etContent.imeOptions = field
        }

    private fun indicateError() {
        tfRoot.background = ContextCompat.getDrawable(context, R.drawable.bg_gears_text_field_error)
        val redColor = ContextCompat.getColor(context, R.color.ui_red)
        tvPrefix.setTextColor(redColor)
        tvSuffix.setTextColor(redColor)
        etContent.setTextColor(redColor)
        etContent.setHintTextColor(redColor)

        if (errorMessage != null) {
            tvHelper.setTextColor(redColor)
            tvHelper.visibility = View.VISIBLE
            tvHelper.text = errorMessage
        }

        ivPrefix.setColorFilter(
            ContextCompat.getColor(context, R.color.ui_red),
            PorterDuff.Mode.SRC_IN
        )
        ivSuffix.setColorFilter(
            ContextCompat.getColor(context, R.color.ui_red),
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun reset() {
        if (error) {
            indicateError()
            return
        }

        flSuffix.background = ContextCompat.getDrawable(
            context,
            R.drawable.bg_gears_text_field_prefix_suffix_regular
        )
        flPrefix.background = ContextCompat.getDrawable(
            context,
            R.drawable.bg_gears_text_field_prefix_suffix_regular
        )
        etContent.setTextColor(ContextCompat.getColor(context, R.color.black_900))
        etContent.setHintTextColor(ContextCompat.getColor(context, R.color.grey_500))

        if (etContent.hasFocus()) {
            focus()
        } else {
            ivPrefix.colorFilter = null
            ivSuffix.colorFilter = null
            tfRoot.background =
                ContextCompat.getDrawable(context, R.drawable.bg_gears_text_field_regular)
            tvPrefix.setTextColor(ContextCompat.getColor(context, R.color.black_500))
            tvSuffix.setTextColor(ContextCompat.getColor(context, R.color.black_500))
        }

        if (helper != null) {
            tvHelper.visibility = View.VISIBLE
            tvHelper.text = helper
            tvHelper.setTextColor(ContextCompat.getColor(context, R.color.black_500))
        } else {
            tvHelper.visibility = View.GONE
        }
    }

    private fun disable() {
        tfRoot.background =
            ContextCompat.getDrawable(context, R.drawable.bg_gears_text_field_disabled)
        flSuffix.background = ContextCompat.getDrawable(
            context,
            R.drawable.bg_gears_text_field_prefix_suffix_disabled
        )
        flPrefix.background = ContextCompat.getDrawable(
            context,
            R.drawable.bg_gears_text_field_prefix_suffix_disabled
        )
        etContent.setTextColor(ContextCompat.getColor(context, R.color.black_500))
        etContent.setHintTextColor(ContextCompat.getColor(context, R.color.white))
    }

    private lateinit var tfRoot: ConstraintLayout
    private lateinit var flPrefix: FrameLayout
    private lateinit var tvPrefix: TextView
    private lateinit var tvSuffix: TextView
    private lateinit var tvLabel: TextView
    private lateinit var tvOptional: TextView
    private lateinit var tvHelper: TextView
    private lateinit var ivPrefix: ImageView
    private lateinit var flSuffix: FrameLayout
    private lateinit var ivSuffix: ImageView
    private lateinit var ivAction: ImageView
    private lateinit var etContent: EditText

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_gears_text_field, this, true)
        initView()
        initAttributes(context, attrs)
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GearsTextField,
            0,
            0
        ).apply {
            try {
                prefixText = getString(R.styleable.GearsTextField_prefixText)
                prefixIcon = getDrawable(R.styleable.GearsTextField_prefixIcon)
                suffixText = getString(R.styleable.GearsTextField_suffixText)
                suffixIcon = getDrawable(R.styleable.GearsTextField_suffixIcon)
                shouldBeEnabled = getBoolean(R.styleable.GearsTextField_android_enabled, true)
                hint = getString(R.styleable.GearsTextField_android_hint)
                inputType = getInteger(R.styleable.GearsTextField_android_inputType, 1)
                enableClear = getBoolean(R.styleable.GearsTextField_enableClear, false)

                val defaultTextColor = context.getColor(R.color.black_500)
                label = getString(R.styleable.GearsTextField_label)
                optional = getBoolean(R.styleable.GearsTextField_optional, false)
                helper = getString(R.styleable.GearsTextField_helper)
                labelColor = getColor(R.styleable.GearsTextField_labelColor, defaultTextColor)
                optionalColor = getColor(R.styleable.GearsTextField_optionalColor, defaultTextColor)
                helperColor = getColor(R.styleable.GearsTextField_helperColor, defaultTextColor)
                minLine = getInt(R.styleable.GearsTextField_android_minLines, 1)
                maxLine = getInt(R.styleable.GearsTextField_android_maxLines, 1)
                maxLength = getInt(R.styleable.GearsTextField_android_maxLength, -1)
            } finally {
                recycle()
            }
        }
    }

    private fun initView() {
        tfRoot = findViewById(R.id.tfRoot)
        flPrefix = findViewById(R.id.flPrefix)
        tvPrefix = findViewById(R.id.tvPrefixText)
        ivPrefix = findViewById(R.id.ivPrefixIcon)
        tvSuffix = findViewById(R.id.tvSuffixText)
        flSuffix = findViewById(R.id.flSuffix)
        ivSuffix = findViewById(R.id.ivSuffixIcon)
        etContent = findViewById(R.id.etTextField)
        tvLabel = findViewById(R.id.gtvLabel)
        tvHelper = findViewById(R.id.gtvHelper)
        tvOptional = findViewById(R.id.gtvLabelOptional)
        ivAction = findViewById(R.id.ivAction)

        etContent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) focus()
            else reset()
        }

        tfRoot.setOnClickListener { etContent.requestFocus() }
        ivAction.setOnClickListener { etContent.setText("") }
    }

    private fun focus() {
        tfRoot.background =
            ContextCompat.getDrawable(context, R.drawable.bg_gears_text_field_focused)
        tvPrefix.setTextColor(ContextCompat.getColor(context, R.color.turqoise))
        tvSuffix.setTextColor(ContextCompat.getColor(context, R.color.turqoise))
        etContent.setTextColor(ContextCompat.getColor(context, R.color.black_900))
        ivPrefix.setColorFilter(
            ContextCompat.getColor(context, R.color.turqoise),
            PorterDuff.Mode.SRC_IN
        )
        ivSuffix.setColorFilter(
            ContextCompat.getColor(context, R.color.turqoise),
            PorterDuff.Mode.SRC_IN
        )
    }

    fun addTextWatcher(textWatcher: TextWatcher) {
        etContent.addTextChangedListener(textWatcher)
    }

    fun removeTextWatcher(textWatcher: TextWatcher) {
        etContent.removeTextChangedListener(textWatcher)
    }

    fun setSelection(position: Int) {
        try {
            etContent.setSelection(position)
        } catch (ex: IndexOutOfBoundsException) {
            val cleanString = text.replace("[.]".toRegex(), "")

            if (cleanString.isNotEmpty()) {
                val parsed = cleanString.toDouble()
                val formatted = NumberFormat.getInstance(Locale("id")).format(parsed)
                setText(formatted)
                setSelection(formatted.length)
            }
        }
    }

    fun doOnTextChanged(callback: (text: CharSequence?, start: Int, count: Int, after: Int) -> Unit) {
        etContent.doOnTextChanged { text, start, count, after ->
            callback(text, start, count, after)
        }
    }

    fun setOnClickListener(listener: (View) -> Unit) {
        tfRoot.setOnClickListener(listener)
        etContent.setOnClickListener(listener)
        etContent.isFocusable = false
    }

    fun setText(text: String?) {
        etContent.setText(text)
    }
}