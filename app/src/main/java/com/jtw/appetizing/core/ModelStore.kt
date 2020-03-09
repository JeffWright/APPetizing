package com.jtw.appetizing.core

import io.reactivex.Observable

/**
 * A ModelStore provides a series of states, updates to which are generated as [Event]s arrive
 */
interface ModelStore<STATE> {
    /** Observable of states */
    val stateObservable: Observable<STATE>

    /** The most recent state, if any */
    val currentState: STATE?

    /** Outputs any effects */
    val effectsObservable: Observable<Effect>

    /** Process an event */
    fun onEvent(event: Event)

    /** Process an effect */
    fun onEffect(effect: Effect)

}