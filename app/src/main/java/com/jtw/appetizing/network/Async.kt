package com.jtw.appetizing.network

import io.reactivex.Observable
import io.reactivex.Single

/** Inspired (from memory) by Airbnb's MvRx library */
sealed class Async<out T> {
    open fun get(): T? = null
}

data class Success<T>(private val value: T) : Async<T>() {
    override fun get(): T = value
    override fun toString() = "Success($value)"
}

class Loading<T> : Async<T>() {
    override fun toString() = "Loading"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

data class Fail<T>(val error: Throwable) : Async<T>() {
    override fun toString() = "Fail(${error.message})"
}

object Uninitialized : Async<Nothing>() {
    override fun toString() = "Uninitialized"
}

fun <T> Single<T>.toAsync(): Observable<Async<T>> {
    return this.map<Async<T>> { Success(it) }
            .onErrorReturn { error -> Fail(error) }
            .toObservable()
            .startWith(Loading())
}