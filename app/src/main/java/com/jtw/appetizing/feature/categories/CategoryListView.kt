package com.jtw.appetizing.feature.categories

import android.view.View
import com.jakewharton.rxrelay2.PublishRelay
import com.jtw.appetizing.core.Event
import com.jtw.appetizing.core.RenderedView
import com.jtw.appetizing.core.RequestLoadCategoriesEvent
import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.network.*
import com.jtw.appetizing.util.hide
import com.jtw.appetizing.util.show
import com.jtw.appetizing.util.standardSetup
import kotlinx.android.synthetic.main.error.view.*
import kotlinx.android.synthetic.main.list.view.*
import kotlinx.android.synthetic.main.loading.view.*
import javax.inject.Inject

class CategoryListView @Inject constructor(
        private val adapter: MealCategoriesAdapter
) : RenderedView<Async<List<MealCategory>>> {

    val itemClicks = adapter.itemClicks
    override val events = PublishRelay.create<Event>()

    override fun bind(view: View) {
        view.recycler.standardSetup(adapter)
    }

    override fun render(view: View, model: Async<List<MealCategory>>) {
        when (model) {
            is Success -> {
                show(view.recycler)
                hide(view.loading, view.error)
                adapter.submitList(model.get())
            }
            is Loading, is Uninitialized -> {
                show(view.loading)
                hide(view.recycler, view.error)
            }
            is Fail -> {
                show(view.error)
                hide(view.recycler, view.loading)

                view.error.retry_button.setOnClickListener {
                    events.accept(RequestLoadCategoriesEvent)
                }
            }
        }
    }

}

