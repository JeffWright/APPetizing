package com.jtw.appetizing.network

import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.domain.MealId
import com.jtw.appetizing.domain.MealWithThumbnail
import com.jtw.appetizing.network.pojo.MealDetails
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Wraps [MealDbRetrofitService] exposing nicer types and [Async] results
 *
 * Also allows simulating poor network conditions
 */
class MealDbService @Inject constructor(
        private val backingService: MealDbRetrofitService
) {
    fun getCategories(): Observable<Async<List<MealCategory>>> {
        return backingService.getCategories()
                .map { it.meals }
                .toAsync()
    }

    fun getMealsForCategory(category: MealCategory): Observable<Async<List<MealWithThumbnail>>> {
        return backingService.getMealsForCategory(category.strCategory)
                .map { it.meals.map { pojo -> MealWithThumbnail(pojo) } }
                .toAsync()
    }

    fun getMealDetails(mealId: MealId): Observable<Async<MealDetails>> {
        return backingService.getMealDetails(mealId)
                .map { it.meals.first() }
                .toAsync()
    }
}