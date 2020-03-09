package com.jtw.appetizing.feature.singlecategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jtw.appetizing.MainActivity
import com.jtw.appetizing.R
import com.jtw.appetizing.core.ChosenCategory
import com.jtw.appetizing.core.DisposableFragment
import com.jtw.appetizing.dagger.MainActivityComponent
import javax.inject.Inject


class MealsListFragment : DisposableFragment() {

    @Inject lateinit var mealsPresenter: MealsListPresenter

    override fun inject(component: MainActivityComponent) {
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.list, container, false)
        view.tag = "MealsListFragment"
        addToDisposable(
                mealsPresenter.bind(view, modelStore)
        )

        setActivityTitle(modelStore.currentState?.chosenCategory)
        return view
    }

    private fun setActivityTitle(chosenCategory: ChosenCategory?) {
        val actualCategory = chosenCategory as? ChosenCategory.Actual ?: return
        activity?.title = actualCategory.category.strCategory
        (activity as? MainActivity)?.showToolbarBackButton(true)
    }

}

