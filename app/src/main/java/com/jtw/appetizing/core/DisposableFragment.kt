package com.jtw.appetizing.core

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jtw.appetizing.MainActivityViewModel
import com.jtw.appetizing.dagger.DaggerFragment
import com.jtw.appetizing.util.plusAssign
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/** Helper that makes it easier to properly dispose of a fragment's rx disposables */
abstract class DisposableFragment : DaggerFragment() {

    protected val modelStore: ModelStore<AppState> by lazy {
        val viewModel: MainActivityViewModel by (activity as AppCompatActivity).viewModels()
        requireNotNull(viewModel.modelStore)
    }

    private var disposable: CompositeDisposable = CompositeDisposable()

    fun addToDisposable(childDisposable: Disposable) {
        disposable += childDisposable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
    }
}