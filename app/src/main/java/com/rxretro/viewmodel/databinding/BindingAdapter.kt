package com.rxretro.viewmodel.databinding

import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.rxretro.view.adapter.ContributorsAdapter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("data")
    fun setData(recyclerView: RecyclerView, data: List<String>?) {
        val recyclerViewAdapter = recyclerView.adapter
        if(recyclerViewAdapter != null && recyclerViewAdapter is ContributorsAdapter){
            recyclerViewAdapter.data = data ?: listOf()
        } else {
            val adapter = ContributorsAdapter()
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.adapter = adapter
        }
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, value: Boolean) {
        view.visibility = if (value) View.VISIBLE else View.GONE
    }
}