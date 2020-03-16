package com.jtw.appetizing.feature.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jtw.appetizing.R
import com.jtw.appetizing.core.DisposableFragment
import com.jtw.appetizing.dagger.MainActivityComponent
import javax.inject.Inject

/** This fragment shows a list of meal categories */
class CategoriesListFragment : DisposableFragment() {

    @Inject lateinit var presenter: CategoryListPresenter

    override fun inject(component: MainActivityComponent) {
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.list, container, false)
        addToDisposable(presenter.bind(view, modelStore))

        return view
    }
}


