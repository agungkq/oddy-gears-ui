package com.oddy.gearsui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.facebook.shimmer.ShimmerFrameLayout

class GearsToolbarMain @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : Toolbar(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_gears_toolbar_main, this, true)
        initView()
    }

    lateinit var gtvInitial: GearsTextView
    lateinit var ivAvatar: ImageView
    lateinit var shimmerAvatar: ShimmerFrameLayout
    private lateinit var ivNotification: ImageView
    private lateinit var tvToolbarNotificationAvatarIndicator: TextView
    private lateinit var tvToolbarNotificationIndicator: TextView

    private fun initView() {
        gtvInitial = findViewById(R.id.gtvHomeInitial)
        ivAvatar = findViewById(R.id.ivToolbarAvatar)
        shimmerAvatar = findViewById(R.id.ivMyAccountAvatarShimmer)
        ivNotification = findViewById(R.id.ivToolbarNotification)
        tvToolbarNotificationAvatarIndicator =
            findViewById(R.id.tvToolbarNotificationAvatarIndicator)
        tvToolbarNotificationIndicator = findViewById(R.id.tvToolbarNotificationIndicator)
    }

    var avatarBadgeValue: Int = 0
        set(value) {
            field = value
            if (value > 0) {
                tvToolbarNotificationAvatarIndicator.text = value.toString()
                tvToolbarNotificationAvatarIndicator.visibility = VISIBLE
            } else tvToolbarNotificationAvatarIndicator.visibility = GONE
        }

    var notificationBadgeValue: Int = 0
        set(value) {
            field = value
            if (value > 0) {
                tvToolbarNotificationIndicator.text = value.toString()
                tvToolbarNotificationIndicator.visibility = VISIBLE
            } else tvToolbarNotificationIndicator.visibility = GONE
        }

    fun setOnAvatarClickedListener(listener: OnClickListener) {
        ivAvatar.setOnClickListener(listener)
        gtvInitial.setOnClickListener(listener)
        shimmerAvatar.setOnClickListener(listener)
    }

    fun setOnNotificationClickedListener(listener: (View) -> Unit) {
        ivNotification.setOnClickListener(listener)
    }
}