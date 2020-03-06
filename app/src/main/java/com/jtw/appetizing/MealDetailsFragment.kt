package com.jtw.appetizing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.jtw.appetizing.dagger.DaggerFragment
import com.jtw.appetizing.dagger.MainActivityComponent
import com.jtw.appetizing.list.ModelStore
import com.jtw.appetizing.network.MealDetails
import com.jtw.appetizing.network.Success
import com.jtw.appetizing.network.ingredients
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.meal_details, container, false)
        view.tag = "MealDetailsFragment"
        addToDisposable(
                presenter.bind(view, modelStore)
        )

        return view
    }


}

class MealDetailsPresenter @Inject constructor() {
    fun bind(view: View, modelStore: ModelStore): Disposable {
        val disposable = CompositeDisposable()

        disposable += modelStore.state
                .mapNotNull { it.chosenMeal }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { meal ->
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
                .subscribe { meal ->
                    view.meal_name.text = meal.get().strMeal
                    view.ingredients.text = meal.get().ingredients()
                            .map { (ingredient, amount) ->
                                "$ingredient  --  $amount"
                            }
                            .joinToString("\n")
                    view.instructions.text = meal.get().strInstructions
                            .replace("\n", "\n\n")

                    Glide.with(view.image)
                            .load(meal.get().strMealThumb)
                            .into(view.image)

                }

        return disposable
    }
}
