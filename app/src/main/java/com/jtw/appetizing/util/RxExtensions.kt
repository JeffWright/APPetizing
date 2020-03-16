package com.jtw.appetizing.util

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

inline fun <reified OUT> Observable<*>.filterIsInstance(): Observable<OUT> = ofType(OUT::class.java)

fun <IN, OUT : Any> Observable<IN>.mapNotNull(mapper: (IN) -> OUT?): Observable<OUT> {
    return this.flatMap { input ->
        val output = mapper(input)
        if (output == null) {
            Observable.empty()
        } else {
            Observable.just(output)
        }
    }
}
