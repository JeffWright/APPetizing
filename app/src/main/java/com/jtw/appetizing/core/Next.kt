package com.jtw.appetizing.core

/**
 * Encapsulates either a new state, or a set of side effects, or both, or neither
 *
 * Inspired (from memory) by Spotify's Mobius library
 */
open class Next<STATE>(
        /** The next state. null indicates no change from previous state */
        val state: STATE? = null,
        /** A list of side effects that should be triggered */
        val effects: List<Effect> = emptyList()
) {

    companion object {
        /** Creates a Next with one effect, and no new state */
        fun <STATE> effect(effect: Effect) = Next<STATE>(effects = listOf(effect))

        /** Creates a Next indicating no new state or effects */
        fun <STATE> noChange() = Next<STATE>(effects = emptyList())
    }
}