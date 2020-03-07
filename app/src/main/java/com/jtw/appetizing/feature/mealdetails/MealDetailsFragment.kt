package com.jtw.appetizing.feature.mealdetails

import android.os.Bundle
import android.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jtw.appetizing.R
import com.jtw.appetizing.core.AppetizingModelStore
import com.jtw.appetizing.dagger.DaggerFragment
import com.jtw.appetizing.dagger.MainActivityComponent
import com.jtw.appetizing.util.plusAssign
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject


// TODO JTW better name
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

class MealDetailsFragment : DisposableFragment() {

    @Inject lateinit var presenter: MealDetailsPresenter
    @Inject lateinit var modelStore: AppetizingModelStore


    override fun inject(component: MainActivityComponent) = component.inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSharedElementEnterTransition(DetailsTransition())
        // setEnterTransition(Fade())
        // setExitTransition(Fade())
        setSharedElementReturnTransition(DetailsTransition())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.meal_details, container, false)
        view.tag = "MealDetailsFragment"
        addToDisposable(
                presenter.bind(view, modelStore)
        )

        return view
    }
}

class DetailsTransition : TransitionSet() {
    init {
        setOrdering(ORDERING_TOGETHER)
        addTransition(ChangeBounds())
        addTransition(ChangeTransform())
        addTransition(ChangeImageTransform())
    }
}

