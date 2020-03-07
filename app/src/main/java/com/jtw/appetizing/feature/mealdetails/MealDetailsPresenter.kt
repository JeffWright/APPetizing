package com.jtw.appetizing.feature.mealdetails

import android.view.View
import com.bumptech.glide.Glide
import com.jtw.appetizing.core.AppetizingModelStore
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

class MealDetailsPresenter @Inject constructor() {
    fun bind(view: View, modelStore: AppetizingModelStore): Disposable {
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