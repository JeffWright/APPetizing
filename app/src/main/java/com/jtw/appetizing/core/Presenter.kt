package com.jtw.appetizing.core

import android.view.View
import com.jtw.appetizing.util.mapNotNull
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

abstract class Presenter<MODEL : Any> {
    abstract val renderedView: RenderedView<MODEL>
    abstract fun AppState.mapToModel(): MODEL?

    open fun bind(view: View, modelStore: ModelStore): Disposable {
        renderedView.bind(view)

        // Call render once synchronously (if we have the data) so that Android's
        // view-state-restoration will work properly
        modelStore.currentState?.mapToModel()
                ?.let { renderedView.render(view, it) }

        return modelStore.stateObservable
                .mapNotNull { it.mapToModel() }
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { model ->
                    renderedView.render(view, model)
                }
    }
}