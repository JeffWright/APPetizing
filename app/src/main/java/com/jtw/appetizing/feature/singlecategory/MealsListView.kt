package com.jtw.appetizing.feature.singlecategory

import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay
import com.jtw.appetizing.core.*
import com.jtw.appetizing.network.*
import com.jtw.appetizing.util.*
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.error.view.*
import kotlinx.android.synthetic.main.list.view.*
import kotlinx.android.synthetic.main.loading.view.*
import javax.inject.Inject

class MealsListView @Inject constructor(
        private val adapter: MealsAdapter
) : RenderedView<ChosenCategory.Actual> {

    override val events = PublishRelay.create<Event>()

    override fun bind(view: View): Disposable {
        view.recycler.standardSetup(adapter)
        return compositeDisposableOf {
            +adapter.itemClicks
                    .subscribe(events)

            +RxView.clicks(view.error.retry_button)
                    .map { RetryCategoryEvent }
                    .subscribe(events)
        }
    }

    override fun render(view: View, model: ChosenCategory.Actual) {
        when (val meals = model.mealsInCategory) {
            is Success -> {
                show(view.recycler)
                hide(view.loading, view.error)

                adapter.submitList(meals.get())
            }
            is Loading, is Uninitialized -> {
                show(view.loading)
                hide(view.recycler, view.error)
            }
            is Fail -> {
                show(view.error)
                hide(view.recycler, view.loading)
            }
        }

    }
}