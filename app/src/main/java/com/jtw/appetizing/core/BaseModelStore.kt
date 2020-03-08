package com.jtw.appetizing.core

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jtw.appetizing.util.plusAssign
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

abstract class BaseModelStore<STATE>(
        initialState: STATE
) : ModelStore<STATE> {
    private val state: BehaviorRelay<STATE> = BehaviorRelay.create()
    private val events: PublishRelay<Event> = PublishRelay.create()
    private val effects: PublishRelay<Effect> = PublishRelay.create()
    protected val disposable = CompositeDisposable()

    override val stateObservable: Observable<STATE> get() = state.hide()
    override val effectsObservable: Observable<Effect> get() = effects.hide()
    override val currentState: STATE? get() = state.value

    init {
        disposable += Observable.zip<STATE, Event, Next<STATE>>(
                        state,
                        events,
                        BiFunction { previousState, event -> reduce(previousState, event) }
                )
                .subscribe { next ->
                    next.effects.forEach { effects.accept(it) }
                    (next.state ?: state.value)?.let {
                        state.accept(it)
                    }
                }

        disposable += effects.subscribe(::handleEffect)

        state.accept(initialState)

        // disposable += state.subscribe { state ->
        //     log("State output: $state ")
        // }
        // disposable += events.subscribe { event ->
        //     log("Event input: $event")
        // }
    }

    override fun onEvent(event: Event) = events.accept(event)
    fun onEffect(event: Effect) = effects.accept(event)

    abstract fun reduce(previousState: STATE, event: Event): Next<STATE>

    abstract fun handleEffect(effect: Effect)
}