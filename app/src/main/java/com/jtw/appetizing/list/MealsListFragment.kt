package com.jtw.appetizing.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jtw.appetizing.*
import com.jtw.appetizing.dagger.MainActivityComponent
import com.jtw.appetizing.network.MealWithThumb
import com.jtw.appetizing.network.Success
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.list.view.*
import javax.inject.Inject


class MealsListFragment : DisposableFragment() {

    @Inject lateinit var categoriesPresenter: CategoryListPresenter
    @Inject lateinit var mealsPresenter: MealListPresenter
    @Inject lateinit var modelStore: ModelStore

    override fun inject(component: MainActivityComponent) {
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.list, container, false)
        view.tag = "MealsListFragment"
        addToDisposable(
                mealsPresenter.bind(view, modelStore)
        )
        return view
    }
}

class MealListPresenter @Inject constructor(
        private val adapter: MealsAdapter
) {

    fun bind(view: View, modelStore: ModelStore): Disposable {
        val disposable = CompositeDisposable()

        val recycler = view.recycler

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(view.context, LinearLayoutManager.VERTICAL)
        recycler.addItemDecoration(dividerItemDecoration)

        val chosenCategoryObservable = modelStore.state
                .map { it.chosenCategory }
                .filterIsInstance<ChosenCategory.Actual>()

        disposable +=
                chosenCategoryObservable
                        .map { "${it.category} meals" }
                        .subscribe(view.header::setText)

        disposable += modelStore.state
                .map { (it.chosenCategory as? ChosenCategory.Actual)?.mealsInCategory }
                .filterIsInstance<Success<*>>()
                .firstElement()
                .subscribe { recycler.scheduleLayoutAnimation() }

        disposable += chosenCategoryObservable
                .subscribe(
                        { state ->
                            val meals: List<MealWithThumb> = state.mealsInCategory.get()
                                    ?: emptyList()
                            adapter.submitList(meals)
                        },
                        {/* TODO JTW */ }
                )

        disposable += adapter.itemClicks
                .subscribe {
                    modelStore.onEvent(ChoseMealEvent(
                            it.idMeal,
                            it.strMeal
                    )) // TODO JTW
                }

        return disposable
    }
}
