package com.jtw.appetizing.feature.singlecategory

import android.view.View
import com.jtw.appetizing.core.ChosenCategory
import com.jtw.appetizing.core.RenderedView
import com.jtw.appetizing.domain.MealWithThumbnail
import com.jtw.appetizing.network.*
import com.jtw.appetizing.util.hide
import com.jtw.appetizing.util.show
import com.jtw.appetizing.util.standardSetup
import io.reactivex.Observable
import kotlinx.android.synthetic.main.error.view.*
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

    private fun setActivityTitle(title: String) {
        // TODO("Not yet implemented")
    }
}