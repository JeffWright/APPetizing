package com.jtw.appetizing.core

import android.widget.ImageView
import android.widget.TextView
import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.domain.MealId
import com.jtw.appetizing.domain.MealWithThumbnail
import com.jtw.appetizing.network.Async
import com.jtw.appetizing.network.pojo.MealDetails

interface Event

/** Request the initial load of available categories */
object RequestLoadCategoriesEvent : Event

/** Signals that the categories data was loaded */
data class LoadedCategoriesEvent(val result: Async<List<MealCategory>>) : Event

/** Signals that the user chose a category */
data class ChoseCategoryEvent(val category: MealCategory) : Event

/** Signals that data for a category was loaded */
data class LoadedMealsForCategoryEvent(val result: Async<List<MealWithThumbnail>>) : Event

/** Signals that the user chose a meal */
data class ChoseMealEvent(
        /** The ID for the meal */
        val mealId: MealId,
        /** Optimization so that the next page can show something while waiting for the network */
        val mealName: String,
        /** Optimization so that the next page can show something while waiting for the network */
        val imageUrl: String,
        /** Text target for shared element transition */
        val sharedElementViewText: TextView,
        /** Image target for shared element transition */
        val sharedElementViewImage: ImageView
) : Event

/** Signals that data for a meal was loaded */
data class LoadedMealDetailsEvent(val result: Async<MealDetails>) : Event

object RetryMealEvent : Event
object RetryCategoryEvent : Event

data class RestoreAppEvent(
        val mealCategory: MealCategory?,
        val mealId: MealId?
) : Event