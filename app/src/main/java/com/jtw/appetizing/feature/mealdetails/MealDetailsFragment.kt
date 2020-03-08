package com.jtw.appetizing.feature.mealdetails

import android.os.Bundle
import android.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jtw.appetizing.MainActivityViewModel
import com.jtw.appetizing.R
import com.jtw.appetizing.core.AppState
import com.jtw.appetizing.core.ModelStore
import com.jtw.appetizing.dagger.MainActivityComponent
import javax.inject.Inject


class MealDetailsFragment : DisposableFragment() {

    companion object {
        const val TAG = "MealDetailsFragment"
    }

    @Inject lateinit var presenter: MealDetailsPresenter

    private val modelStore: ModelStore<AppState> by lazy {
        val viewModel: MainActivityViewModel by (activity as AppCompatActivity).viewModels()
        requireNotNull(viewModel.modelStore)
    }


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

