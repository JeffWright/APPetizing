package com.jtw.appetizing.domain

/** Matches the JSON output of TheMealDB */
class MealWithThumbnailPojo(
        val strMeal: String,
        val strMealThumb: String,
        val idMeal: MealId
)

data class MealWithThumbnail(val pojo: MealWithThumbnailPojo) {
    val strMeal: String = pojo.strMeal
    val mealThumb: String = pojo.strMealThumb
    val idMeal: MealId = pojo.idMeal
}