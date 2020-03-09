package com.jtw.appetizing.core

import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.domain.MealId

/**
 * An Effect triggers an external non-pure side-effect, e.g. a network call or a navigation
 * transition
 */
interface Effect

object LoadCategoriesEffect : Effect

data class LoadMealsEffect(val category: MealCategory) : Effect

data class LoadMealDetailsEffect(val mealId: MealId) : Effect
