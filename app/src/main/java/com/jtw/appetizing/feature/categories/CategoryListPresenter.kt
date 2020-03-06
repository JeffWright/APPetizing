package com.jtw.appetizing.feature.categories

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jtw.appetizing.core.ChoseCategoryEvent
import com.jtw.appetizing.core.ModelStore
import com.jtw.appetizing.network.Success
import com.jtw.appetizing.util.filterIsInstance
import com.jtw.appetizing.util.log
import com.jtw.appetizing.util.plusAssign
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.list.view.*
import javax.inject.Inject

class CategoryListPresenter @Inject constructor(
        private val adapter: MealCategoriesAdapter
) {

    fun bind(view: View, modelStore: ModelStore): Disposable {
        val disposable = CompositeDisposable()

        val recycler = view.recycler

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(view.context, LinearLayoutManager.VERTICAL)
        recycler.addItemDecoration(dividerItemDecoration)

        disposable += adapter.itemClicks
                .subscribe {
                    modelStore.onEvent(ChoseCategoryEvent(it))
                }

        disposable += modelStore.state
                .map { it.categories }
                .filterIsInstance<Success<*>>()
                .firstElement()
                .subscribe { recycler.scheduleLayoutAnimation() }

        disposable += modelStore.state
                .subscribe(
                        { state ->
                            val items = state.categories.get()
                            // recycler.scheduleLayoutAnimation()
                            log("submit: $items")
                            adapter.submitList(items ?: emptyList())
                            log("submitted")
                        },
                        {/* TODO JTW */ }
                )

        return disposable
    }
}