package com.jtw.appetizing.core

import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.domain.MealId
import com.jtw.appetizing.domain.MealWithThumbnail
import com.jtw.appetizing.network.Async
import com.jtw.appetizing.network.pojo.MealDetails

interface Event

object RequestLoadCategoriesEvent : Event
data class LoadedCategoriesEvent(val result: Async<List<MealCategory>>) : Event

data class ChoseCategoryEvent(val category: MealCategory) : Event
data class LoadedMealsForCategoryEvent(val result: Async<List<MealWithThumbnail>>) : Event

data class ChoseMealEvent(
        val mealId: MealId,
        /** Optimization so that the next page can show something while waiting for the network */
        val mealName: String,
        /** Optimization so that the next page can show something while waiting for the network */
        val imageUrl: String
) : Event

data class LoadedMealDetailsEvent(val result: Async<MealDetails>) : Event
