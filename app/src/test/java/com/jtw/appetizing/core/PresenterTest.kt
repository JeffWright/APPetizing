package com.jtw.appetizing.core

import android.view.View
import com.jtw.appetizing.domain.MealId
import com.jtw.appetizing.network.Uninitialized
import com.jtw.appetizing.util.shouldBe
import com.jtw.appetizing.util.shouldContain
import com.jtw.appetizing.util.synchronousScheduler
import io.mockk.mockk
import io.reactivex.Scheduler
import org.junit.Before
import org.junit.Test

class PresenterTest {


    class SomePresenter : Presenter<String>() {

        override fun deliveryScheduler(): Scheduler = synchronousScheduler()

        override val renderedView = RenderedViewStub<String>()

        override fun AppState.mapToModel(): String? {
            return this.chosenMeal?.mealName
        }
    }

    private lateinit var objectUnderTest: SomePresenter
    private val view = mockk<View>(relaxed = true)
    private lateinit var modelStore: ModelStoreStub

    private val sampleMeal = ChosenMeal(
            mealId = MealId("ID"),
            mealName = "MEAL",
            mealDetails = Uninitialized,
            imageUrl = "URL"
    )

    @Before
    fun setup() {
        modelStore = ModelStoreStub()

        objectUnderTest = SomePresenter()
    }

    @Test
    fun `delivers model to view`() {
        objectUnderTest.bind(view, modelStore)

        modelStore.stateRelay.accept(AppState(Uninitialized, chosenMeal = sampleMeal))

        objectUnderTest.renderedView.boundView shouldBe view
        objectUnderTest.renderedView.renderedModels.size shouldBe 1
        objectUnderTest.renderedView.renderedModels shouldContain "MEAL"
    }

    @Test
    fun `does not call render repeatedly with unchanged values`() {
        objectUnderTest.bind(view, modelStore)

        repeat(10) {
            modelStore.stateRelay.accept(AppState(Uninitialized, chosenMeal = sampleMeal))
        }
        repeat(10) {
            modelStore.stateRelay.accept(AppState(Uninitialized, chosenMeal = sampleMeal.copy(mealName = "OTHER")))
        }
        repeat(10) {
            modelStore.stateRelay.accept(AppState(Uninitialized, chosenMeal = sampleMeal))
        }

        objectUnderTest.renderedView.boundView shouldBe view
        objectUnderTest.renderedView.renderedModels.size shouldBe 3
    }
}

