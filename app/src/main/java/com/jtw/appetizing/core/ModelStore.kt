package com.jtw.appetizing.core

import io.reactivex.Observable

interface ModelStore {
    val stateObservable: Observable<AppState>
    val currentState: AppState?
    val effectsObservable: Observable<Effect>

    fun onEvent(event: Event)
}