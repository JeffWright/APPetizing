package com.jtw.appetizing

import android.view.View
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
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                        )
                        .replace(R.id.container, MealsListFragment())
                        .addToBackStack(null)
                        .commit();
            }
            is ShowMealDetailsEffect -> {
                // fragmentManager.replaceFragment(
                //         MealDetailsFragment(),
                //         container)

                fragmentManager.beginTransaction()
                        // .addSharedElement(sharedElementView, sharedElementViewName)
                        .setCustomAnimations(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                        )
                        .replace(R.id.container, MealDetailsFragment())
                        .addToBackStack(null)
                        .commit();
            }
        }
    }
}

lateinit var sharedElementView: View // view in first fragment
val sharedElementViewName: String = "image"

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

