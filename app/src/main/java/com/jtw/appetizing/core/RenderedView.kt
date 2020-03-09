package com.jtw.appetizing.core

import android.view.View
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/** Wraps an Android View and renders changes into it based on [MODEL] */
interface RenderedView<MODEL> {
    /** Exposes the events that this view would like to send */
    val events: Observable<Event>

    /** Sets the View to be rendered */
    fun bind(view: View): Disposable?

    /** Updates the View based on the MODEL */
    fun render(view: View, model: MODEL)
}