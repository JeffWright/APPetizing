package com.jtw.appetizing

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.jtw.appetizing.list.ChosenCategory
import com.jtw.appetizing.list.ModelStore
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class Navigator @Inject constructor(
        private val fragmentManager: FragmentManager,
        private val modelStore: ModelStore
) {

    private val disposable = CompositeDisposable()
    lateinit var container: ViewGroup
    private var navigationState: NavigationStateMachine = CategoriesList

    fun bind(container: ViewGroup) {
        this.container = container

        /*
        disposable += modelStore.state
                .map { it.chosenCategory }
                .map { it is ChosenCategory.Actual }
                .filter { it }
                .distinctUntilChanged()
                .subscribe {
                    fragmentManager.replaceFragment(
                            MealsListFragment(),
                            container
                    )
                }

         */

        disposable += modelStore.state
                .map { it.chosenCategory }
                .filterIsInstance<ChosenCategory.Actual>()
                .subscribe {
                    (navigationState as? CategoriesList)
                            ?.toMealsList()
                }
    }

}

sealed class NavigationStateMachine

object CategoriesList : NavigationStateMachine() {
    fun toMealsList() {
        TODO("toMealsList() not implemented")
    }
}

object MealsList : NavigationStateMachine() {
    fun toMealDetails() {
        TODO("toMealDetails() not implemented")
    }
}

object MealDetails : NavigationStateMachine()

