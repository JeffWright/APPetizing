package com.jtw.appetizing.network

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface MealDbRetrofitService {
    companion object {
        const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
    }

    @GET("list.php?c=list")
    fun getCategories(): Single<TheMealDbPojo<MealCategory>>

    @GET("filter.php")
    fun getMealsForCategory(@Query("c") category: String): Single<TheMealDbPojo<MealWithThumb>>

    @GET("lookup.php")
    fun getMealDetails(@Query("i") mealId: String): Single<TheMealDbPojo<MealDetails>>
}

class MealDbService @Inject constructor(
        private val backingService: MealDbRetrofitService
) {
    fun getCategories(): Observable<Async<List<String>>> {
        return backingService.getCategories()
                .map { it.meals.map { it.strCategory } }
                .toAsync()
    }

    fun getMealsForCategory(category: String): Observable<Async<List<MealWithThumb>>> {
        return backingService.getMealsForCategory(category)
                .map { it.meals }
                .toAsync()
    }

    fun getMealDetails(mealId: String): Observable<Async<MealDetails>> {
        return backingService.getMealDetails(mealId)
                .map { it.meals.first() }
                .toAsync()
    }
}

