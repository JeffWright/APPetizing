package com.jtw.appetizing.core

import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.domain.MealId
import com.jtw.appetizing.network.Success
import com.jtw.appetizing.network.Uninitialized
import com.jtw.appetizing.network.pojo.MealDetails
import com.jtw.appetizing.util.*
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class AppetizingModelStoreTest {

    private val initialSate = AppState(categories = Uninitialized)
    private val objectUnderTest = AppetizingModelStore(initialSate, mockk())

    private val testState = AppState(
            categories = Success(listOf(MealCategory("cat1"), MealCategory("cat2")))
    )

    @Before
    fun setup() {
        logger = {}
    }
   
    @Test
    fun `handles RequestLoadCategoriesEvent`() {
        val result = objectUnderTest.reduce(testState, RequestLoadCategoriesEvent)

        result.state shouldBe null
        result.effects shouldContain LoadCategoriesEffect
    }

    @Test
    fun `handles LoadedCategoriesEvent`() {
        val result = objectUnderTest.reduce(testState, LoadedCategoriesEvent(Success(listOf())))

        result.state!!.categories shouldBe Success(listOf())
        result.effects shouldBe emptyList()
    }

    @Test
    fun `handles ChoseCategoryEvent`() {
        val result = objectUnderTest.reduce(testState, ChoseCategoryEvent(MealCategory("cat1")))

        result.state!!.chosenCategory shouldBe ChosenCategory.Actual(MealCategory("cat1"))
        result.effects shouldContain LoadMealsEffect(MealCategory("cat1"))
        result.effects shouldContain ShowMealsListEffect
    }

    @Test
    fun `handles LoadedMealsForCategoryEvent`() {
        val testStateWithCategory = testState.copy(
                chosenCategory = ChosenCategory.Actual(MealCategory("cat1"))
        )

        val result = objectUnderTest.reduce(testStateWithCategory, LoadedMealsForCategoryEvent(Success(listOf())))

        result.state!!.chosenCategory.shouldBeInstance<ChosenCategory.Actual>()
        result.effects shouldBe emptyList()
    }

    @Test
    fun `handles ChoseMealEvent`() {
        val mealId = MealId("ID")

        val result = objectUnderTest.reduce(testState, ChoseMealEvent(
                mealId,
                "mealName",
                "url",
                mockk(),
                mockk()
        ))

        result.state!!.chosenMeal shouldBe ChosenMeal(mealId, "mealName", "url")
        result.effects shouldContain LoadMealDetailsEffect(mealId)
        result.effects.shouldContainInstance<ShowMealDetailsEffect>()
    }

    @Test
    fun `handles LoadedMealDetailsEvent`() {
        val mealId = MealId("ID")
        val testStateWithCategory = testState.copy(
                chosenMeal = ChosenMeal(mealId, "mealName", "url")
        )
        val mealDetails = mockk<MealDetails>()

        val result = objectUnderTest.reduce(testStateWithCategory, LoadedMealDetailsEvent(Success(mealDetails)))

        result.state!!.chosenMeal?.mealDetails?.get() shouldBe mealDetails
        result.effects shouldBe emptyList()
    }
}
