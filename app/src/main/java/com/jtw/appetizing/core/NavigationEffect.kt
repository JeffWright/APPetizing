package com.jtw.appetizing.core

import android.widget.ImageView
import android.widget.TextView
import com.jtw.appetizing.feature.mealdetails.MealDetailsFragment
import com.jtw.appetizing.feature.singlecategory.MealsListFragment


sealed class NavigationEffect : Effect

/**
 * Show the list of meals for a given category
 * @see MealsListFragment
 */
object ShowMealsListEffect : NavigationEffect()

/**
 * Show the detail view for a given meal
 * @see MealDetailsFragment
 */
data class ShowMealDetailsEffect(
        /** Text target for shared element transition */
        val sharedElementViewText: TextView,
        /** Image target for shared element transition */
        val sharedElementViewImage: ImageView
) : NavigationEffect()
