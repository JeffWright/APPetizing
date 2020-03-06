package com.jtw.appetizing.core

/** Inspired (from memory) by Spotify's Mobius library */
open class Next<STATE>(
        /** null means no change from previous state */
        val state: STATE? = null,
        val effects: List<Effect> = emptyList()
) {

    companion object {
        fun <STATE> effect(effect: Effect) = Next<STATE>(effects = listOf(effect))
        fun <STATE> effects(effects: List<Effect>) = Next<STATE>(effects = effects)

        // class effects<STATE>(effects: List<Effect>) = Next<STATE>(effects)
        //
        fun <STATE> noChange() = Next<STATE>(effects = emptyList())
    }
}