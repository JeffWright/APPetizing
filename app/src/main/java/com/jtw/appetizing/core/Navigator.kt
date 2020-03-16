package com.jtw.appetizing.core

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jtw.appetizing.R
import com.jtw.appetizing.dagger.ActivityScoped
import com.jtw.appetizing.feature.mealdetails.MealDetailsFragment
import com.jtw.appetizing.feature.singlecategory.MealsListFragment
import com.jtw.appetizing.util.filterIsInstance
import com.jtw.appetizing.util.plusAssign
import com.jtw.appetizing.util.transaction
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Handles navigation transitions between screens
 */
@ActivityScoped
class Navigator @Inject constructor(
        private val fragmentManager: FragmentManager
) {

    companion object {
        /** Names for Shared Element Transitions */
        const val transitionNameImage: String = "imageTransition"
        const val transitionNameText: String = "textTransition"
    }

    private val disposable = CompositeDisposable()
    @IdRes private var primaryContainerId: Int = 0
    @IdRes private var secondaryContainerId: Int = 0
    private var isTwoPane: Boolean = false

    /** Attach this Navigator to the given layouts, and listen to [modelStore] for [NavigationEffect]s */
    fun bind(modelStore: ModelStore<AppState>, @IdRes primaryContainerId: Int, @IdRes secondaryContainerId: Int) {
        unbind()

        this.primaryContainerId = primaryContainerId
        this.secondaryContainerId = secondaryContainerId
        isTwoPane = primaryContainerId != secondaryContainerId

        disposable += modelStore.effectsObservable
                .filterIsInstance<NavigationEffect>()
                .subscribe(::handleEffect)
    }

    /** Detach this Navigator, and clear its subscription to the ModelStore */
    fun unbind() {
        disposable.clear()
    }

    private fun handleEffect(effect: NavigationEffect) {
        when (effect) {
            is ShowMealsListEffect -> {
                fragmentManager.transaction {
                    setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    )
                    replace(primaryContainerId, MealsListFragment())
                }
            }
            is ShowMealDetailsEffect -> {
                fragmentManager.transaction(backstack = !isTwoPane) {
                    addSharedElement(effect.sharedElementViewText, transitionNameText)
                    addSharedElement(effect.sharedElementViewImage, transitionNameImage)
                    setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    )
                    replace(secondaryContainerId, MealDetailsFragment(), MealDetailsFragment.TAG)
                }
            }
        }
    }
}


