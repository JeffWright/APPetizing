package com.jtw.appetizing

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.jtw.appetizing.core.*
import com.jtw.appetizing.feature.mealdetails.MealDetailsFragment
import com.jtw.appetizing.feature.singlecategory.MealsListFragment
import com.jtw.appetizing.util.filterIsInstance
import com.jtw.appetizing.util.plusAssign
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
                fragmentManager.beginTransaction()
                        .addSharedElement(sharedElementViewText, transitionNameText)
                        .addSharedElement(sharedElementViewImage, transitionNameImage)
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

// TODO JTW
lateinit var sharedElementViewText: View
lateinit var sharedElementViewImage: View
val transitionNameImage: String = "imageTransition"
val transitionNameText: String = "textTransition"

