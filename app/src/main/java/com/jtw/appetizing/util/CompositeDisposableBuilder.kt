package com.jtw.appetizing.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/** Provides an easy way to build up a CompositeDisposable */
class CompositeDisposableBuilder {
    val composite = CompositeDisposable()
    operator fun Disposable?.unaryPlus() {
        this ?: return
        composite += this
    }
}

/**
 *  Provides an easy way to build up a CompositeDisposable.  For example:
 *  ```
 *  compositeDisposableOf {
 *      +disposable1
 *      +disposable2
 *  }
 *  ```
 */
inline fun compositeDisposableOf(block: CompositeDisposableBuilder.() -> Unit): CompositeDisposable {
    val builder = CompositeDisposableBuilder()
    builder.block()
    return builder.composite
}
