package com.jtw.appetizing

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.jtw.appetizing.list.Effect
import com.jtw.appetizing.list.MealsListFragment
import com.jtw.appetizing.list.ModelStore
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class Navigator @Inject constructor(
        private val fragmentManager: FragmentManager,
        private val modelStore: ModelStore
) {

    private val disposable = CompositeDisposable()
    lateinit var container: ViewGroup

    fun bind(container: ViewGroup) {
        this.container = container

        disposable += modelStore.effects
                .filterIsInstance<NavigationEffect>()
                .subscribe(::handleEffect)


    }

    private fun handleEffect(effect: NavigationEffect) {
        when (effect) {
            is ShowMealsListEffect -> {
                fragmentManager.replaceFragment(
                        MealsListFragment(),
                        container)
            }
            is ShowMealDetailsEffect -> {
                fragmentManager.replaceFragment(
                        MealDetailsFragment(),
                        container)
            }
        }
    }
}

sealed class NavigationEffect : Effect
class ShowMealsListEffect : NavigationEffect()
class ShowMealDetailsEffect : NavigationEffect()

/*
sealed class NavigationStateMachine

object CategoriesList : NavigationStateMachine() {
    fun toMealsList(
            fragmentManager: FragmentManager,
            container: ViewGroup
    ): NavigationStateMachine {

        return MealsList
    }
}

object MealsList : NavigationStateMachine() {
    fun toMealDetails() {
        TODO("toMealDetails() not implemented")
    }
}

object MealDetails : NavigationStateMachine()
 */

