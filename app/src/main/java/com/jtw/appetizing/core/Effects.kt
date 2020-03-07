package com.jtw.appetizing.core

import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.domain.MealId

interface Effect

sealed class NavigationEffect : Effect
object ShowMealsListEffect : NavigationEffect()
object ShowMealDetailsEffect : NavigationEffect()


object LoadCategoriesEffect : Effect
data class LoadMealsEffect(val category: MealCategory) : Effect
data class LoadMealDetailsEffect(val mealId: MealId) : Effect
