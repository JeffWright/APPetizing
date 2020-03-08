package com.jtw.appetizing.feature.singlecategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jtw.appetizing.MainActivity
import com.jtw.appetizing.MainActivityViewModel
import com.jtw.appetizing.R
import com.jtw.appetizing.core.ChosenCategory
import com.jtw.appetizing.core.ModelStore
import com.jtw.appetizing.dagger.MainActivityComponent
import com.jtw.appetizing.feature.mealdetails.DisposableFragment
import javax.inject.Inject


class MealsListFragment : DisposableFragment() {

    @Inject lateinit var mealsPresenter: MealListPresenter

    private val modelStore: ModelStore by lazy {
        val viewModel: MainActivityViewModel by (activity as AppCompatActivity).viewModels()
        requireNotNull(viewModel.modelStore)
    }

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

        val chosenCategory = modelStore.currentState?.chosenCategory as? ChosenCategory.Actual
        requireNotNull(chosenCategory)
        activity?.title = chosenCategory.category.strCategory
        (activity as? MainActivity)?.showToolbarBackButton(true)
        return view
    }

}

