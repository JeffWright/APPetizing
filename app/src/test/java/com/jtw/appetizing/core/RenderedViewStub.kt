package com.jtw.appetizing.core

import android.view.View
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.disposables.Disposable

class RenderedViewStub<T> : RenderedView<T> {

    var boundView: View? = null
    override fun bind(view: View): Disposable? {
        boundView = view
        return null
    }

    val renderedModels = mutableListOf<T>()
    override fun render(view: View, model: T) {
        renderedModels += model
    }

    override val events = PublishRelay.create<Event>()
}