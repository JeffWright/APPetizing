package com.jtw.appetizing.domain

import com.jtw.appetizing.network.pojo.MealWithThumbnailPojo

/** Wraps a [MealWithThumbnailPojo] and exposes some cleaner names for those fields */
data class MealWithThumbnail(val pojo: MealWithThumbnailPojo) {
    val name: String = pojo.strMeal
    val thumbnailUrl: String = pojo.strMealThumb
    val id: MealId = pojo.idMeal
}