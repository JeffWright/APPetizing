package com.jtw.appetizing.core

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

class ModelStoreStub : ModelStore<AppState> {

    val stateRelay = BehaviorRelay.create<AppState>()
    val effectsRelay = PublishRelay.create<Effect>()
    val eventsRelay = PublishRelay.create<Event>()

    override val stateObservable = stateRelay
    override val currentState: AppState? get() = stateRelay.value
    override val effectsObservable: Observable<Effect> get() = effectsRelay
    override fun onEvent(event: Event) {
    }

}