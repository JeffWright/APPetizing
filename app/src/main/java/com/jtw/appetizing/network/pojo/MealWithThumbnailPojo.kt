package com.jtw.appetizing.network.pojo

import com.jtw.appetizing.domain.MealId

/** Matches the JSON output of TheMealDB, so that Moshi can deserialize automatically */
class MealWithThumbnailPojo(
        val strMeal: String,
        val strMealThumb: String,
        val idMeal: MealId
)