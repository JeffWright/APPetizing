package com.jtw.appetizing.core

import android.view.View
import com.jtw.appetizing.util.compositeDisposableOf
import com.jtw.appetizing.util.mapNotNull
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * A Presenter takes a (Android) [View], a [RenderedView], and a [ModelStore].  It provides a
 * mapping from the larger AppState to the specific model type required by the [RenderedView]
 *
 * It ensures that the [RenderedView]'s render() is only called when the model actually changes,
 * and that render is called once immediately if possible (to play nicely with Android's view state
 * system)
 */
abstract class Presenter<MODEL : Any> {

    /** The [RenderedView] which should be driven by this presenter */
    abstract val renderedView: RenderedView<MODEL>

    /** Extract the appropriate [MODEL] from an [AppState] */
    abstract fun AppState.mapToModel(): MODEL?

    /**
     * Allows you to set the Rx scheduler on which models should be delivered to the
     * [RenderedView].  Primarily useful for testing.
     */
    protected open fun deliveryScheduler(): Scheduler = AndroidSchedulers.mainThread()

    /** Binds a view, which will be rendered based on the states output by the ModelStore */
    open fun bind(view: View, modelStore: ModelStore<AppState>): Disposable {
        return compositeDisposableOf {
            +renderedView.bind(view)

            // Call render once synchronously (if we have the data) so that Android's
            // view-state-restoration will work properly
            modelStore.currentState?.mapToModel()
                    ?.let { renderedView.render(view, it) }


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