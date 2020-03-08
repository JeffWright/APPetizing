package com.jtw.appetizing.feature.singlecategory

import android.view.View
import com.jtw.appetizing.core.*
import com.jtw.appetizing.util.compositeDisposableOf
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MealListPresenter @Inject constructor(
        override val renderedView: MealsListView
) : Presenter<ChosenCategory.Actual>() {

    override fun AppState.mapToModel() = chosenCategory as? ChosenCategory.Actual

    override fun bind(view: View, modelStore: ModelStore<AppState>): Disposable {
        return compositeDisposableOf {
            +super.bind(view, modelStore)

            // TODO seems like we should be able to combine these
            +renderedView.itemClicks
                    .subscribe(modelStore::onEvent)
            //
            // +renderedView.events
            //         .subscribe(modelStore::onEvent)
        }
    }
}
