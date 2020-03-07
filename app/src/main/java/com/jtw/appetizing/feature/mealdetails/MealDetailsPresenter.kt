package com.jtw.appetizing.feature.mealdetails

import android.view.View
import com.jtw.appetizing.core.ModelStore
import com.jtw.appetizing.util.mapNotNull
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MealDetailsPresenter @Inject constructor(
        private val mealDetailsView: MealDetailsView
) {
    fun bind(view: View, modelStore: ModelStore): Disposable {
        mealDetailsView.bind(view)

        // Call render once synchronously (if we have the data) so that Android's
        // view-state-restoration will work properly
        (modelStore.currentState?.chosenMeal)
                ?.let { mealDetailsView.render(view, it) }

        return modelStore.stateObservable
                .mapNotNull { it.chosenMeal }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { meal ->
                    mealDetailsView.render(view, meal)
                }
    }
}