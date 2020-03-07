package com.jtw.appetizing.feature.singlecategory

import android.view.View
import com.jtw.appetizing.core.ChoseMealEvent
import com.jtw.appetizing.core.ChosenCategory
import com.jtw.appetizing.core.ModelStore
import com.jtw.appetizing.util.compositeDisposableOf
import com.jtw.appetizing.util.filterIsInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MealListPresenter @Inject constructor(
        private val mealsListView: MealsListView
) {

    fun bind(view: View, modelStore: ModelStore, setActivityTitle: (String) -> Unit): Disposable {

        mealsListView.bind(view)

        // Call render once synchronously (if we have the data) so that Android's
        // view-state-restoration will work properly
        (modelStore.currentState?.chosenCategory as? ChosenCategory.Actual)
                ?.let { mealsListView.render(view, it) }

        return compositeDisposableOf {
            +modelStore.stateObservable
                    .map { it.chosenCategory }
                    .filterIsInstance<ChosenCategory.Actual>()
                    .distinctUntilChanged()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { chosenCategory ->
                        mealsListView.render(view, chosenCategory)
                    }

            +mealsListView.itemClicks
                    .map { ChoseMealEvent(it.idMeal, it.strMeal, it.mealThumb) }
                    .subscribe(modelStore::onEvent)
        }
    }
}
