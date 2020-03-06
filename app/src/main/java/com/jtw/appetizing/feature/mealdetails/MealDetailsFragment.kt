package com.jtw.appetizing.feature.mealdetails

import android.os.Bundle
import android.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.jtw.appetizing.R
import com.jtw.appetizing.core.ModelStore
import com.jtw.appetizing.dagger.DaggerFragment
import com.jtw.appetizing.dagger.MainActivityComponent
import com.jtw.appetizing.network.Success
import com.jtw.appetizing.network.pojo.MealDetails
import com.jtw.appetizing.network.pojo.ingredients
import com.jtw.appetizing.util.filterIsInstance
import com.jtw.appetizing.util.mapNotNull
import com.jtw.appetizing.util.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.meal_details.view.*
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
    @Inject lateinit var modelStore: ModelStore


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

class MealDetailsPresenter @Inject constructor() {
    fun bind(view: View, modelStore: ModelStore): Disposable {
        val disposable = CompositeDisposable()

        view.ingredients_and_instructions.alpha = 0f

        // disposable +=
        modelStore.state
                .mapNotNull { it.chosenMeal }
                // .observeOn(AndroidSchedulers.mainThread())
                // .subscribe { meal ->
                .blockingFirst().let { meal ->
                    view.meal_name.text = meal.mealName
                    Glide.with(view.image)
                            .load(meal.imageUrl)
                            .into(view.image)
                }

        disposable += modelStore.state
                .filter { it.chosenMeal != null }
                .map { it.chosenMeal!!.mealDetails }
                .filterIsInstance<Success<MealDetails>>()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { loadedMeal ->
                    val meal = loadedMeal.get()

                    view.meal_name.text = meal.strMeal

                    if (view.ingredients_and_instructions.alpha == 0f) {
                        view.ingredients_and_instructions.animate()
                                .alpha(1f)
                                .setDuration(250)
                    }

                    view.ingredients.text = meal.ingredients()
                            .map { (ingredient, amount) ->
                                "$ingredient  --  $amount"
                            }
                            .joinToString("\n")

                    view.instructions.text = meal.strInstructions
                            .replace("\n", "\n\n")

                    Glide.with(view.image)
                            .load(meal.strMealThumb)
                            .into(view.image)

                }

        return disposable
    }
}
