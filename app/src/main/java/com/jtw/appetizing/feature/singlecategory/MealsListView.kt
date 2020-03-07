package com.jtw.appetizing.feature.singlecategory

import android.view.View
import com.jtw.appetizing.core.ChosenCategory
import com.jtw.appetizing.domain.MealWithThumbnail
import com.jtw.appetizing.network.*
import io.reactivex.Observable
import kotlinx.android.synthetic.main.list.view.*
import javax.inject.Inject

class MealsListView @Inject constructor(
        private val adapter: MealsAdapter
) {

    fun bind(view: View) {
        val recycler = view.recycler
        recycler.standardSetup(adapter)
    }

    fun render(view: View, category: ChosenCategory.Actual) {

        setActivityTitle(category.category.strCategory)

        val meals = category.mealsInCategory
        when (meals) {
            is Success -> adapter.submitList(meals.get())
            is Loading, is Uninitialized -> {
                /* TODO JTW */
            }
            is Fail -> {
                /* TODO JTW */
            }
        }

    }

    val itemClicks: Observable<MealWithThumbnail> = adapter.itemClicks

    private fun setActivityTitle(title: String) {
        // TODO("Not yet implemented")
    }
}