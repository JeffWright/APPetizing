package com.jtw.appetizing.feature.mealdetails

import com.jtw.appetizing.dagger.DaggerFragment
import com.jtw.appetizing.util.plusAssign
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class DisposableFragment : DaggerFragment() {
    private var disposable: CompositeDisposable? = CompositeDisposable()

    fun addToDisposable(childDisposable: Disposable) {
        val disposable = disposable ?: CompositeDisposable()
        disposable += childDisposable
        this.disposable = disposable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.clear()
        disposable = null
    }
}