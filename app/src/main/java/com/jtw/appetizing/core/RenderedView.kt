package com.jtw.appetizing.core

import android.view.View

// TODO not the best name but "View" is taken...
interface RenderedView<MODEL> {
    fun bind(view: View)
    fun render(view: View, model: MODEL)
}