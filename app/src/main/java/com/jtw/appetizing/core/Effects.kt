package com.jtw.appetizing.core

import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.domain.MealId

interface Effect

sealed class NavigationEffect : Effect
object ShowMealsListEffect : NavigationEffect()
object ShowMealDetailsEffect : NavigationEffect()


object LoadCategoriesEffect : Effect
class LoadMealsEffect(val category: MealCategory) : Effect
class LoadMealDetailsEffect(val mealId: MealId) : Effect
