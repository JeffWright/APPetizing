package com.jtw.appetizing.network

import com.jtw.appetizing.domain.MealId
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

/**
 * Wraps [MealDbRetrofitService] exposing nicer types and [Async] results
 *
 * Also allows simulating poor network conditions
 */
class FlakyMealDbService @Inject constructor(
        private val backingService: MealDbRetrofitService
) : MealDbRetrofitService {

    @Suppress("UNUSED")
    private fun <T> Single<T>.makeFlaky(): Single<T> {
        val randInt = Random.nextInt(0, 100)
        return if (randInt <= 13) {
            this
        } else if (randInt <= 46) {
            val millis = Random.nextInt(0, 5000)
            this.delay(millis.toLong(), TimeUnit.MILLISECONDS)
        } else {
            val millis = Random.nextInt(0, 5000)
            this.delay(millis.toLong(), TimeUnit.MILLISECONDS)
                    .makeFail()
        }
    }

    @Suppress("UNUSED")
    private fun <T> Single<T>.makeFail(): Single<T> {
        return this.map { throw Exception("Manufactured Exception") }
    }

    override fun getCategories() = backingService.getCategories()
            .makeFlaky()

    override fun getMealsForCategory(category: String) = backingService.getMealsForCategory(category)
            .makeFlaky()

    override fun getMealDetails(mealId: MealId) = backingService.getMealDetails(mealId)
            .makeFlaky()
}