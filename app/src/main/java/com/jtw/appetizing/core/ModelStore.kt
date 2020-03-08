package com.jtw.appetizing.core

import io.reactivex.Observable

interface ModelStore<STATE> {
    val stateObservable: Observable<STATE>
    val currentState: STATE?
    val effectsObservable: Observable<Effect>

    fun onEvent(event: Event)
}