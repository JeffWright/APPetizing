package com.jtw.appetizing.feature.categories

import android.view.View
import com.jtw.appetizing.core.ChoseCategoryEvent
import com.jtw.appetizing.core.ModelStore
import com.jtw.appetizing.util.compositeDisposableOf
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class CategoryListPresenter @Inject constructor(
        private val categoryListView: CategoryListView
) {

    fun bind(view: View, modelStore: ModelStore): Disposable {
        categoryListView.bind(view)

        // Call render once synchronously (if we have the data) so that Android's
        // view-state-restoration will work properly
        modelStore.currentState?.categories
                ?.let { categoryListView.render(view, it) }

        return compositeDisposableOf {
            +categoryListView.itemClicks
                    .subscribe {
                        modelStore.onEvent(ChoseCategoryEvent(it))
                    }

            +modelStore.stateObservable
                    .map { it.categories }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { categoryListView.render(view, it) }
        }
    }
}