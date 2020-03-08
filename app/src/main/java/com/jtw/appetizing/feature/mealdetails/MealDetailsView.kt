package com.jtw.appetizing.feature.mealdetails

import android.view.View
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay
import com.jtw.appetizing.core.*
import com.jtw.appetizing.network.*
import com.jtw.appetizing.network.pojo.ingredients
import com.jtw.appetizing.network.pojo.tags
import com.jtw.appetizing.util.hide
import com.jtw.appetizing.util.show
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.error.view.*
import kotlinx.android.synthetic.main.loading.view.*
import kotlinx.android.synthetic.main.meal_details.view.*
import javax.inject.Inject

class MealDetailsView @Inject constructor() : RenderedView<ChosenMeal> {

    override val events = PublishRelay.create<Event>()

    override fun bind(view: View): Disposable {
        view.ingredients_and_instructions.alpha = 0f

        return RxView.clicks(view.error.retry_button)
                .map { RetryMealEvent }
                .subscribe(events)
    }

    override fun render(view: View, model: ChosenMeal) {
        view.meal_name.text = model.mealName

        Glide.with(view.image)
                .load(model.imageUrl)
                .into(view.image)

        when (val details = model.mealDetails) {
            is Success -> {
                show(view.ingredients_and_instructions)
                hide(view.error, view.loading)

                val meal = details.get()

                view.ingredients_and_instructions.visibility = View.VISIBLE
                view.loading.visibility = View.GONE

                view.ingredients_and_instructions.animate()
                        .alpha(1f)
                        .setDuration(250)

                view.ingredients.text = meal.ingredients()
                        .map { (ingredient, amount) ->
                            "$ingredient  --  $amount"
                        }
                        .joinToString("\n")

                view.instructions.text = meal.strInstructions
                        .replace("\n", "\n\n")

                view.tags.text = meal.tags().joinToString("  |  ")


            }
            is Uninitialized, is Loading -> {
                show(view.loading)
                hide(view.error, view.ingredients_and_instructions)
            }
            is Fail -> {
                show(view.error)
                hide(view.ingredients_and_instructions, view.loading)
            }
        }
    }
}