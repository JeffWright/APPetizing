package com.jtw.appetizing.network

import com.jtw.appetizing.domain.*
import com.jtw.appetizing.network.pojo.MealDetails
import com.jtw.appetizing.network.pojo.TheMealDbPojo
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

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

class MealDbService @Inject constructor(
        private val backingService: MealDbRetrofitService
) {
    fun getCategories(): Observable<Async<List<MealCategory>>> {
        return backingService.getCategories()
                .map { it.meals }
                .makeFlaky()
                .toAsync()
    }

    fun getMealsForCategory(category: MealCategory): Observable<Async<List<MealWithThumbnail>>> {
        return backingService.getMealsForCategory(category.strCategory)
                .map { it.meals.map { pojo -> MealWithThumbnail(pojo) } }
                .makeFlaky()
                // .makeFail()
                .toAsync()
    }

    fun getMealDetails(mealId: MealId): Observable<Async<MealDetails>> {
        return backingService.getMealDetails(mealId)
                .map { it.meals.first() }
                .makeFlaky()
                .toAsync()
    }

    @Suppress("UNUSED")
    private fun <T> Single<T>.makeFlaky(): Single<T> {
        val randInt = Random.nextInt(0, 100)
        return if (randInt <= 113) {
            this
        } else if (randInt <= 46) {
            val millis = Random.nextInt(0, 3000)
            this.delay(millis.toLong(), TimeUnit.MILLISECONDS)
        } else {
            val millis = Random.nextInt(0, 3000)
            this.delay(millis.toLong(), TimeUnit.MILLISECONDS)
                    .makeFail()
        }
    }

    @Suppress("UNUSED")
    private fun <T> Single<T>.makeFail(): Single<T> {
        return this.map { throw Exception("Manufactured Exception") }
    }
}

