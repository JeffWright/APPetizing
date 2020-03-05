package com.jtw.appetizing.network

import io.reactivex.Observable
import io.reactivex.Single

sealed class Async<out T> {
    open fun get(): T? = null
}

class Success<T>(private val value: T) : Async<T>() {
    override fun get(): T = value
    override fun toString() = "Success($value)"
}

class Loading<T> : Async<T>() {
    override fun toString() = "Loading"
}

class Fail<T>(val error: Throwable) : Async<T>() {
    override fun toString() = "Fail(${error.message})"
}

object Uninitialized : Async<Nothing>() {
    override fun toString() = "Uninitialized"
}

fun <T, R> Async<T>.flatMap(block: (T) -> R): Async<R> {
    return when (this) {
        is Success -> Success(block(this.get()))
        is Loading -> this as Loading<R>
        is Fail -> this as Fail<R>
        is Uninitialized -> this as Async<R>
    }
}

fun <T> Single<T>.toAsync(): Observable<Async<T>> {
    return this.map { Success(it) as Async<T> }
            .onErrorReturn { error -> Fail(error) }
            .toObservable()
            .startWith(Loading())
}