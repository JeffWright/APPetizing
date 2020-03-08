package com.jtw.appetizing.core

import android.view.View
import com.jtw.appetizing.util.compositeDisposableOf
import com.jtw.appetizing.util.mapNotNull
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

abstract class Presenter<MODEL : Any> {
    abstract val renderedView: RenderedView<MODEL>
    abstract fun AppState.mapToModel(): MODEL?

    protected open fun deliveryScheduler(): Scheduler = AndroidSchedulers.mainThread()

    open fun bind(view: View, modelStore: ModelStore<AppState>): Disposable {
        renderedView.bind(view)

        // Call render once synchronously (if we have the data) so that Android's
        // view-state-restoration will work properly
        modelStore.currentState?.mapToModel()
                ?.let { renderedView.render(view, it) }

        return compositeDisposableOf {

            +modelStore.stateObservable
                    .mapNotNull { it.mapToModel() }
                    .distinctUntilChanged()
                    .observeOn(deliveryScheduler())
                    .subscribe { model ->
                        renderedView.render(view, model)
                    }

            +renderedView.events.subscribe(modelStore::onEvent)
        }
    }
}