package com.jtw.appetizing.core

import android.view.View

class RenderedViewStub<T> : RenderedView<T> {

    var boundView: View? = null
    override fun bind(view: View) {
        boundView = view
    }

    val renderedModels = mutableListOf<T>()
    override fun render(view: View, model: T) {
        renderedModels += model
    }
}