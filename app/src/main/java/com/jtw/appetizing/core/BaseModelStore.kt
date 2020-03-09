package com.jtw.appetizing.core

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jtw.appetizing.BuildConfig
import com.jtw.appetizing.util.compositeDisposableOf
import com.jtw.appetizing.util.log
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
        disposable += compositeDisposableOf {

            if (BuildConfig.DEBUG) {
                +events.subscribe { event ->
                    log("--> event: $event")
                }
                +effects.subscribe { effect ->
                    log("<-- effect: $effect")
                }
                +state.subscribe { state ->
                    log("STATE: $state ")
                }
            }

            // Create a cyclical observable, where outputs from state are reduced based on
            // sequential events, yielding a new state (which goes back into [state]) and/or a set
            // of effects
            +Observable.zip<STATE, Event, Next<STATE>>(
                            state,
                            events,
                            BiFunction { previousState, event -> reduce(previousState, event) }
                    )
                    .subscribe { next ->
                        next.effects.forEach { effects.accept(it) }
                        (next.state ?: state.value)?.let { state.accept(it) }
                    }

            +effects.subscribe(::handleEffect)
        }

        state.accept(initialState)
    }

    override fun onEvent(event: Event) = events.accept(event)

    override fun onEffect(effect: Effect) = effects.accept(effect)

    /**
     * The core of the ModelStore. Given the most recent state and an incoming [Event], returns
     * an updated state and/or a series of [Effect]s to trigger
     */
    abstract fun reduce(previousState: STATE, event: Event): Next<STATE>

    /**
     * Requests that the given effect be processed
     */
    abstract fun handleEffect(effect: Effect)
}