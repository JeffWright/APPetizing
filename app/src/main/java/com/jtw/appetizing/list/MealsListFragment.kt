package com.jtw.appetizing.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jtw.appetizing.R
import com.jtw.appetizing.dagger.DaggerFragment
import com.jtw.appetizing.dagger.MainActivityComponent
import com.jtw.appetizing.filterIsInstance
import com.jtw.appetizing.plusAssign
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.list.view.*
import javax.inject.Inject

class MealsListFragment : DaggerFragment() {

    @Inject lateinit var categoriesPresenter: CategoryListPresenter
    @Inject lateinit var mealsPresenter: MealListPresenter
    @Inject lateinit var modelStore: ModelStore

    override fun inject(component: MainActivityComponent) {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.list, container, false)
        view.tag = "MealsListFragment"
        mealsPresenter.bind(view, modelStore)
        return view
    }

}

class MealListPresenter @Inject constructor(
        private val adapter: MyAdapter
) {

    fun bind(view: View, modelStore: ModelStore): Disposable {
        val disposable = CompositeDisposable()

        val recycler = view.recycler

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)

        val chosenCategoryObservable = modelStore.state
                .map { it.chosenCategory }
                .filterIsInstance<ChosenCategory.Actual>()

        disposable +=
                chosenCategoryObservable
                        .map { "${it.category} meals" }
                        .subscribe(view.header::setText)

        disposable += chosenCategoryObservable
                .subscribe(
                        { state ->
                            val meals: List<String> = state.mealsInCategory.get()
                                    ?.map { it.strMeal }
                                    ?: emptyList()
                            adapter.submitList(meals)
                        },
                        {/* TODO JTW */ }
                )

        disposable += adapter.itemClicks
                .subscribe {
                    modelStore.onEvent(ChoseMealEvent(mealId = "52920")) // TODO JTW
                }

        return disposable
    }
}
