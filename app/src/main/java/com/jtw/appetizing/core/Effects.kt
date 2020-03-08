package com.jtw.appetizing.core

import android.widget.ImageView
import android.widget.TextView
import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.domain.MealId

interface Effect

sealed class NavigationEffect : Effect
object ShowMealsListEffect : NavigationEffect()
data class ShowMealDetailsEffect(
        /** Text target for shared element transition */
        val sharedElementViewText: TextView,
        /** Image target for shared element transition */
        val sharedElementViewImage: ImageView
) : NavigationEffect()


object LoadCategoriesEffect : Effect
data class LoadMealsEffect(val category: MealCategory) : Effect
data class LoadMealDetailsEffect(val mealId: MealId) : Effect
