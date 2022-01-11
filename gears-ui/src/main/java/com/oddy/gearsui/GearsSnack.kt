package com.oddy.gearsui

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar


object GearsSnack {
    fun success(context: Context, root: View, @StringRes message: Int) =
        success(context, root, context.getString(message))

    fun success(context: Context, root: View, message: String) {
        val view = View.inflate(context, R.layout.layout_gears_snack, null)
        view.findViewById<TextView>(R.id.gtvSnackMessage).text =
            HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY)

        val snackBar = Snackbar.make(root, "", Snackbar.LENGTH_LONG)

        val margin = (30 * Resources.getSystem().displayMetrics.density).toInt()
        snackBar.view.setPadding(0, 0, 0, margin)
        snackBar.view.elevation = 0f

        (snackBar.view as ViewGroup).run {
            removeAllViews()
            addView(view)
        }

        snackBar.setBackgroundTint(ContextCompat.getColor(context, android.R.color.transparent))
        snackBar.show()
    }
}