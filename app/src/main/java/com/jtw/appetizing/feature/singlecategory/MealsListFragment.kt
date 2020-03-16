package com.jtw.appetizing.feature.singlecategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jtw.appetizing.R
import com.jtw.appetizing.TitleProvider
import com.jtw.appetizing.core.ChosenCategory
import com.jtw.appetizing.core.DisposableFragment
import com.jtw.appetizing.dagger.MainActivityComponent
import javax.inject.Inject


class MealsListFragment : DisposableFragment(), TitleProvider {

    @Inject lateinit var mealsPresenter: MealsListPresenter

    override fun inject(component: MainActivityComponent) {
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.list, container, false)
        addToDisposable(
                mealsPresenter.bind(view, modelStore)
        )

        return view
    }

    override val title: CharSequence?
        get() {
            val chosenCategory = modelStore.currentState?.chosenCategory as? ChosenCategory.Actual
            return chosenCategory?.category?.strCategory
        }

}

