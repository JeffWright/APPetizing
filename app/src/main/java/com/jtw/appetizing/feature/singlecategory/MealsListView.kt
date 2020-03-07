package com.jtw.appetizing.feature.singlecategory

import android.view.View
import com.jtw.appetizing.core.ChosenCategory
import com.jtw.appetizing.core.RenderedView
import com.jtw.appetizing.domain.MealWithThumbnail
import com.jtw.appetizing.network.*
import com.jtw.appetizing.util.standardSetup
import io.reactivex.Observable
import kotlinx.android.synthetic.main.list.view.*
import kotlinx.android.synthetic.main.loading.view.*
import javax.inject.Inject

class MealsListView @Inject constructor(
        private val adapter: MealsAdapter
) : RenderedView<ChosenCategory.Actual> {

    val itemClicks: Observable<MealWithThumbnail> = adapter.itemClicks

    override fun bind(view: View) {
        view.recycler.standardSetup(adapter)
    }

    override fun render(view: View, category: ChosenCategory.Actual) {

        setActivityTitle(category.category.strCategory)

        val meals = category.mealsInCategory
        when (meals) {
            is Success -> {
                view.loading.visibility = View.GONE
                view.recycler.visibility = View.VISIBLE
                adapter.submitList(meals.get())
            }
            is Loading, is Uninitialized -> {
                view.loading.visibility = View.VISIBLE
                view.recycler.visibility = View.GONE
            }
            is Fail -> {
                /* TODO JTW */
            }
        }

    }

    private fun setActivityTitle(title: String) {
        // TODO("Not yet implemented")
    }
}