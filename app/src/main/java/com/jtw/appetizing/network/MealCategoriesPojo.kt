package com.jtw.appetizing.network

data class MealCategoriesPojo(
        val meals: List<MealCategory>
)

data class MealCategory(
        val strCategory: String
)

data class MealsPojo(
        val meals: List<MealWithThumb>
)

data class MealWithThumb(
        val strMeal: String,
        val strMealThumb: String,
        val idMeal: String
)