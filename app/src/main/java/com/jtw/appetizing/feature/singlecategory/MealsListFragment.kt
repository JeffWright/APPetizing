package com.jtw.appetizing.feature.singlecategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jtw.appetizing.R
import com.jtw.appetizing.core.AppetizingModelStore
import com.jtw.appetizing.dagger.MainActivityComponent
import com.jtw.appetizing.feature.categories.CategoryListPresenter
import com.jtw.appetizing.feature.mealdetails.DisposableFragment
import javax.inject.Inject


class MealsListFragment : DisposableFragment() {

    @Inject lateinit var categoriesPresenter: CategoryListPresenter
    @Inject lateinit var mealsPresenter: MealListPresenter
    @Inject lateinit var modelStore: AppetizingModelStore

    override fun inject(component: MainActivityComponent) {
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.list, container, false)
        view.tag = "MealsListFragment"
        addToDisposable(
                mealsPresenter.bind(view, modelStore) {
                    activity?.title = getString(R.string.title_meals, it)
                }
        )
        return view
    }
}

