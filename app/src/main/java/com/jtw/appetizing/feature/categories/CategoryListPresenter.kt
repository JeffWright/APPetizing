package com.jtw.appetizing.feature.categories

import android.view.View
import com.jtw.appetizing.core.*
import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.network.Async
import com.jtw.appetizing.util.compositeDisposableOf
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class CategoryListPresenter @Inject constructor(
        override val renderedView: CategoryListView
) : Presenter<Async<List<MealCategory>>>() {

    override fun AppState.mapToModel() = categories

    override fun bind(view: View, modelStore: ModelStore): Disposable {
        return compositeDisposableOf {
            +super.bind(view, modelStore)

            +renderedView.itemClicks
                    .subscribe {
                        modelStore.onEvent(ChoseCategoryEvent(it))
                    }
        }
    }
}