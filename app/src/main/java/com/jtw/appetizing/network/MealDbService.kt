package com.jtw.appetizing.network

import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.domain.MealId
import com.jtw.appetizing.domain.MealWithThumbnail
import com.jtw.appetizing.network.pojo.MealDetails
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

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