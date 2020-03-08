package com.jtw.appetizing.feature.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jtw.appetizing.MainActivity
import com.jtw.appetizing.MainActivityViewModel
import com.jtw.appetizing.R
import com.jtw.appetizing.core.ModelStore
import com.jtw.appetizing.dagger.MainActivityComponent
import com.jtw.appetizing.feature.mealdetails.DisposableFragment
import javax.inject.Inject

class CategoriesListFragment : DisposableFragment() {

    @Inject lateinit var presenter: CategoryListPresenter

    private val modelStore: ModelStore by lazy {
        val viewModel: MainActivityViewModel by (activity as AppCompatActivity).viewModels()
        requireNotNull(viewModel.modelStore)
    }

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
        activity?.title = getString(R.string.app_name)
        (activity as? MainActivity)?.showToolbarBackButton(false)
        return view
    }
}


