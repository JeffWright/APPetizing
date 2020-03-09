package com.jtw.appetizing.network

import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.domain.MealId
import com.jtw.appetizing.network.pojo.MealDetails
import com.jtw.appetizing.network.pojo.MealWithThumbnailPojo
import com.jtw.appetizing.network.pojo.TheMealDbPojo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MealDbRetrofitService {
    companion object {
        const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
    }

    @GET("list.php?c=list")
    fun getCategories(): Single<TheMealDbPojo<MealCategory>>

    @GET("filter.php")
    fun getMealsForCategory(@Query("c") category: String): Single<TheMealDbPojo<MealWithThumbnailPojo>>

    @GET("lookup.php")
    fun getMealDetails(@Query("i") mealId: MealId): Single<TheMealDbPojo<MealDetails>>
}

