package com.jtw.appetizing.util

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.standardSetup(adapter: RecyclerView.Adapter<*>) {
    this.adapter = adapter
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
    addItemDecoration(dividerItemDecoration)
}
