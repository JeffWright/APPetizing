package com.jtw.appetizing.feature.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jtw.appetizing.R
import com.jtw.appetizing.core.AppetizingModelStore
import com.jtw.appetizing.dagger.MainActivityComponent
import com.jtw.appetizing.feature.mealdetails.DisposableFragment
import javax.inject.Inject

class CategoriesListFragment : DisposableFragment() {

    @Inject lateinit var presenter: CategoryListPresenter
    @Inject lateinit var modelStore: AppetizingModelStore

    override fun inject(component: MainActivityComponent) {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.list, container, false)
        view.tag = "CategoriesListFragment"
        addToDisposable(
                presenter.bind(view, modelStore)
        )
        activity?.title = getString(R.string.categories)
        return view
    }
}


