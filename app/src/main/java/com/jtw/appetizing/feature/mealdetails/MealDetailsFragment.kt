package com.jtw.appetizing.feature.mealdetails

import android.os.Bundle
import android.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jtw.appetizing.R
import com.jtw.appetizing.core.DisposableFragment
import com.jtw.appetizing.dagger.MainActivityComponent
import com.jtw.appetizing.util.hide
import javax.inject.Inject


/** This fragment shows a detail view for a particular meal */
class MealDetailsFragment : DisposableFragment() {

    companion object {
        const val TAG = "MealDetailsFragment"
    }

    @Inject lateinit var presenter: MealDetailsPresenter

    override fun inject(component: MainActivityComponent) = component.inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = DetailsTransition()
        sharedElementReturnTransition = DetailsTransition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.meal_details, container, false)
        addToDisposable(
                presenter.bind(view, modelStore)
        )

        hide(container?.findViewById(R.id.no_meal_selected))

        return view
    }
}

class DetailsTransition : TransitionSet() {
    init {
        ordering = ORDERING_TOGETHER
        addTransition(ChangeBounds())
        addTransition(ChangeTransform())
        addTransition(ChangeImageTransform())
    }
}

