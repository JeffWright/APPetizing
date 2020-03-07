package com.jtw.appetizing.util

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.standardSetup(adapter: RecyclerView.Adapter<*>) {
    this.adapter = adapter
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
    addItemDecoration(dividerItemDecoration)
}

fun show(view: View?) {
    view?.visibility = View.VISIBLE
}

fun hide(vararg views: View?) {
    views.forEach { it?.visibility = View.GONE }
}
