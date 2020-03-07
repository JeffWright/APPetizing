package com.jtw.appetizing.core

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jtw.appetizing.util.log
import com.jtw.appetizing.util.plusAssign
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

abstract class BaseModelStore : ModelStore {
    protected val state: BehaviorRelay<AppState> = BehaviorRelay.create() // TODO JTW private
    private val events: PublishRelay<Event> = PublishRelay.create()
    private val effects: PublishRelay<Effect> = PublishRelay.create()
    protected val disposable = CompositeDisposable()

    override val stateObservable: Observable<AppState> get() = state.hide()
    override val effectsObservable: Observable<Effect> get() = effects.hide()
    override val currentState: AppState? get() = state.value

    init {
        disposable += Observable.zip<AppState, Event, Next<AppState>>(
                        state.observeOn(Schedulers.single()), // TODO JTW
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

        disposable += state.subscribe { state ->
            log("State output: $state ")
        }
        disposable += events.subscribe { event ->
            log("Event input: $event")
        }
    }

    override fun onEvent(event: Event) = events.accept(event)
    fun onEffect(event: Effect) = effects.accept(event)

    abstract fun reduce(previousState: AppState, event: Event): Next<AppState>

    abstract fun handleEffect(effect: Effect)
}