package com.oddy.gearsui

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class GearsCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var cornerRadius: Int = RADIUS_DEFAULT
        set(value) {
            val availableRadius =
                arrayOf(RADIUS_DEFAULT, RADIUS_FLAT, RADIUS_MAXIMAL, RADIUS_MINIMAL)
            if (availableRadius.contains(value)) {
                field = value
                updateBackground()
            }
        }

    var isBordered: Boolean = false
        set(value) {
            field = value
            updateBackground()
        }

    var cardElevation: Int = ELEVATION_NORMAL
        set(value) {
            val availableRadius = arrayOf(ELEVATION_NONE, ELEVATION_FLOAT, ELEVATION_NORMAL)
            if (!availableRadius.contains(value)) return
            field = value
            val elevationId = when (value) {
                ELEVATION_NORMAL -> R.dimen.gears_card_view_elevation_normal
                ELEVATION_FLOAT -> R.dimen.gears_card_view_elevation_float
                else -> R.dimen.gears_card_view_elevation_none
            }
            elevation = context.resources.getDimensionPixelSize(elevationId).toFloat()
        }

    private fun updateBackground() {
        val backgroundDrawable = when (cornerRadius) {
            RADIUS_FLAT ->
                if (isBordered) R.drawable.bg_gears_card_view_flat_bordered
                else R.drawable.bg_gears_card_view_flat
            RADIUS_MINIMAL ->
                if (isBordered) R.drawable.bg_gears_card_view_minimal_bordered
                else R.drawable.bg_gears_card_view_minimal
            RADIUS_MAXIMAL ->
                if (isBordered) R.drawable.bg_gears_card_view_maximal_bordered
                else R.drawable.bg_gears_card_view_maximal
            else ->
                if (isBordered) R.drawable.bg_gears_card_view_default_bordered
                else R.drawable.bg_gears_card_view_default
        }
        background = ContextCompat.getDrawable(context, backgroundDrawable)
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GearsCardView,
            0,
            0
        ).apply {
            try {
                cornerRadius =
                    getInteger(R.styleable.GearsCardView_gcv_cornerRadius, RADIUS_DEFAULT)
                cardElevation =
                    getInteger(R.styleable.GearsCardView_gcv_elevation, ELEVATION_NORMAL)
                isBordered = getBoolean(R.styleable.GearsCardView_gcv_isBordered, false)
            } finally {
                recycle()
            }
        }
    }

    companion object {
        const val RADIUS_FLAT = 0
        const val RADIUS_MINIMAL = 1
        const val RADIUS_DEFAULT = 2
        const val RADIUS_MAXIMAL = 3

        const val ELEVATION_NONE = 0
        const val ELEVATION_NORMAL = 1
        const val ELEVATION_FLOAT = 2
    }
}