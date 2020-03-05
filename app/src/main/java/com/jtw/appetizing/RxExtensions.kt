package com.jtw.appetizing

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified OUT> Observable<*>.filterIsInstance(): Observable<OUT> {
    return this.filter { it is OUT } as Observable<OUT>
}
