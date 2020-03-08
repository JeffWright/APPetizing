package com.jtw.appetizing.core

import android.view.View
import io.reactivex.Observable

interface RenderedView<MODEL> {
    val events: Observable<Event>
    fun bind(view: View)
    fun render(view: View, model: MODEL)
}