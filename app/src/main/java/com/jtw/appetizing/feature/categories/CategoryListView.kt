package com.jtw.appetizing.feature.categories

import android.view.View
import com.jtw.appetizing.core.RenderedView
import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.network.*
import com.jtw.appetizing.util.standardSetup
import kotlinx.android.synthetic.main.list.view.*
import kotlinx.android.synthetic.main.loading.view.*
import javax.inject.Inject

class CategoryListView @Inject constructor(
        private val adapter: MealCategoriesAdapter
) : RenderedView<Async<List<MealCategory>>> {

    val itemClicks = adapter.itemClicks

    override fun bind(view: View) {
        view.recycler.standardSetup(adapter)
    }

    override fun render(view: View, model: Async<List<MealCategory>>) {
        when (model) {
            is Success -> {
                view.loading.visibility = View.GONE
                view.recycler.visibility = View.VISIBLE
                adapter.submitList(model.get())
            }
            is Loading, is Uninitialized -> {
                view.loading.visibility = View.VISIBLE
                // view.recycler.visibility = View.GONE
            }
            is Fail -> {
                /* TODO JTW */
            }
        }
    }

}