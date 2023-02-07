package com.ecoatm.devicepickup.utils

import android.app.Activity
import android.widget.TextView
import com.ecoatm.devicepickup.R

fun Activity.setToolbarTitle(title: String) {
    val titleTextView = findViewById<TextView>(R.id.title)
    titleTextView?.text = title
}