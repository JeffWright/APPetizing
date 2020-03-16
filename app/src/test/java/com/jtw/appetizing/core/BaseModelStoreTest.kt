package com.jtw.appetizing.core

import com.jtw.appetizing.util.logger
import com.jtw.appetizing.util.shouldBe
import org.junit.Before
import org.junit.Test

class BaseModelStoreTest {

    data class SimpleState(
            val string: String,
            val number: Int
    )

    object AddOneEvent : Event
    object EvenEffect : Effect

    class SimpleModelStore : BaseModelStore<SimpleState>(SimpleState("hello", 0)) {
        override fun reduce(previousState: SimpleState, event: Event): Next<SimpleState> {
            if (event is AddOneEvent) {
                return if (previousState.number % 2 == 0) {
                    Next(previousState.copy(number = previousState.number + 1), effects = listOf(EvenEffect))
                } else {
                    Next(previousState.copy(number = previousState.number + 1))
                }
            }

            return Next.noChange()
        }

        val seenEffects = mutableListOf<Effect>()
        override fun handleEffect(effect: Effect) {
            seenEffects += effect
        }
    }

    @Before
    fun setup() {
        logger = {}
    }

    @Test
    fun `events are handled strictly sequentially`() {
        val objectUnderTest = SimpleModelStore()

        objectUnderTest.currentState.number shouldBe 0

        repeat(100) {
            Thread().run {
                objectUnderTest.onEvent(AddOneEvent)
                objectUnderTest.onEvent(AddOneEvent)
                objectUnderTest.onEvent(AddOneEvent)
            }
        }

        Thread.sleep(100)

        objectUnderTest.currentState.number shouldBe 100 * 3
        objectUnderTest.seenEffects.size shouldBe 100 * 3 / 2
    }
}