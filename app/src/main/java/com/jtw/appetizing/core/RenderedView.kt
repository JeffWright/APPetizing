package com.jtw.appetizing.core

import android.view.View
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface RenderedView<MODEL> {
    val events: Observable<Event>
    fun bind(view: View): Disposable
    fun render(view: View, model: MODEL)
}