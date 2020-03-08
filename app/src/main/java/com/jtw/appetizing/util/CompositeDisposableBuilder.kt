package com.jtw.appetizing.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class CompositeDisposableBuilder {
    val composite = CompositeDisposable()
    operator fun Disposable?.unaryPlus() {
        this ?: return
        composite += this
    }
}

inline fun compositeDisposableOf(block: CompositeDisposableBuilder.() -> Unit): CompositeDisposable {
    val builder = CompositeDisposableBuilder()
    builder.block()
    return builder.composite
}
