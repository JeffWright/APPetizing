package com.jtw.appetizing.core

import android.view.View

interface RenderedView<MODEL> {
    fun bind(view: View)
    fun render(view: View, model: MODEL)
}