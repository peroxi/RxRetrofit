package com.rxretro.viewmodel.databinding

import android.databinding.BindingAdapter
import android.view.View

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, value: Boolean) {
        view.visibility = if (value) View.VISIBLE else View.GONE
    }
}