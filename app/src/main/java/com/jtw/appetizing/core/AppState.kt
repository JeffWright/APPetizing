package com.jtw.appetizing.core

import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.domain.MealId
import com.jtw.appetizing.domain.MealWithThumbnail
import com.jtw.appetizing.network.Async
import com.jtw.appetizing.network.Uninitialized
import com.jtw.appetizing.network.pojo.MealDetails

data class AppState(
        val categories: Async<List<MealCategory>>,
        val chosenCategory: ChosenCategory = ChosenCategory.None,
        val chosenMeal: ChosenMeal? = null
)

sealed class ChosenCategory {

    data class Actual(
            val category: MealCategory,
            val mealsInCategory: Async<List<MealWithThumbnail>> = Uninitialized
    ) : ChosenCategory()

    object None : ChosenCategory()
}

data class ChosenMeal(
        val mealId: MealId,
        val mealName: String,
        val imageUrl: String,
        val mealDetails: Async<MealDetails> = Uninitialized
)

