package com.jtw.appetizing.feature.singlecategory

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jtw.appetizing.core.ChoseMealEvent
import com.jtw.appetizing.core.ChosenCategory
import com.jtw.appetizing.core.ModelStore
import com.jtw.appetizing.domain.MealWithThumbnail
import com.jtw.appetizing.network.Success
import com.jtw.appetizing.util.filterIsInstance
import com.jtw.appetizing.util.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.list.view.*
import javax.inject.Inject

class MealListPresenter @Inject constructor(
        private val adapter: MealsAdapter
) {

    fun bind(view: View, modelStore: ModelStore, setActivityTitle: (String) -> Unit): Disposable {
        val disposable = CompositeDisposable()

        val recycler = view.recycler

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(view.context, LinearLayoutManager.VERTICAL)
        recycler.addItemDecoration(dividerItemDecoration)

        val chosenCategoryObservable = modelStore.state
                .map { it.chosenCategory }
                .filterIsInstance<ChosenCategory.Actual>()
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())

        disposable +=
                chosenCategoryObservable
                        .map { it.category }
                        .subscribe { category -> setActivityTitle(category.strCategory) }

        disposable += modelStore.state
                .map { (it.chosenCategory as? ChosenCategory.Actual)?.mealsInCategory }
                .filterIsInstance<Success<*>>()
                .firstElement()
                .subscribe { recycler.scheduleLayoutAnimation() }

        disposable += chosenCategoryObservable
                .subscribe(
                        { state ->
                            val meals: List<MealWithThumbnail> = state.mealsInCategory.get()
                                    ?: emptyList()
                            adapter.submitList(meals)
                        },
                        {/* TODO JTW */ }
                )

        disposable += adapter.itemClicks
                .subscribe {
                    modelStore.onEvent(ChoseMealEvent(
                            it.idMeal,
                            it.strMeal,
                            it.mealThumb
                    )) // TODO JTW
                }

        return disposable
    }
}