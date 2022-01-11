package com.oddy.gearsui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class GearsToolbarTitle @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {
    var title: String? = null
        set(value) {
            field = value
            tvTitle.text = value
        }

    var subtitle: String? = null
        set(value) {
            field = value
            if (value == null) {
                tvSubtitle.visibility = View.GONE
                tvTitle.size = GearsTextView.HIGH_SPEED_20
            } else {
                tvSubtitle.visibility = View.VISIBLE
                tvSubtitle.text = value
                tvTitle.size = GearsTextView.HIGH_SPEED_18
            }
        }

    var drawableEnd: Drawable? = null
        set(value) {
            field = value
            if (value == null) {
                ivDrawableEnd.visibility = View.GONE
            } else {
                ivDrawableEnd.visibility = View.VISIBLE
                ivDrawableEnd.setImageDrawable(value)
                ivDrawableEnd.setColorFilter(context.getColor(R.color.white))
            }
        }

    var hideDrawableStart: Boolean = false
        set(value) {
            field = value
            ivBack.visibility = if (value) View.INVISIBLE else View.VISIBLE
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_gears_toolbar_title, this, true)
        initView()
        initAttributes(context, attrs)
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GearsToolbarTitle,
            0,
            0
        ).apply {
            try {
                title = getString(R.styleable.GearsToolbarTitle_title)
                subtitle = getString(R.styleable.GearsToolbarTitle_subtitle)
                hideDrawableStart =
                    getBoolean(R.styleable.GearsToolbarTitle_hideDrawableStart, false)
                drawableEnd = getDrawable(R.styleable.GearsToolbarTitle_android_drawableEnd)
            } finally {
                recycle()
            }
        }
    }

    private lateinit var tvTitle: GearsTextView
    private lateinit var tvSubtitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var ivDrawableEnd: ImageView
    private lateinit var ivNotificationIndicator: ImageView

    private fun initView() {
        tvTitle = findViewById(R.id.gtvToolbarTitle)
        tvSubtitle = findViewById(R.id.gtvToolbarSubtitle)
        ivBack = findViewById(R.id.ivToolbarBack)
        ivDrawableEnd = findViewById(R.id.ivToolbarNotification)
        ivNotificationIndicator = findViewById(R.id.ivToolbarNotificationIndicator)
    }

    fun setOnBackIconClickedListener(listener: OnClickListener) {
        ivBack.setOnClickListener(listener)
    }

    fun senOnDrawableEndClickedListener(listener: OnClickListener) {
        ivDrawableEnd.setOnClickListener(listener)
    }
}