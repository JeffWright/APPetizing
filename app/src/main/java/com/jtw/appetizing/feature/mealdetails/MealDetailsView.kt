package com.jtw.appetizing.feature.mealdetails

import android.view.View
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay
import com.jtw.appetizing.R
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

        // These are both wrapped in a FrameLayout to play nicely with the CoordinatorLayout, so
        // their visibility is toggled by that instead of directly
        show(view.error)
        show(view.loading)

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
                hide(view.error_frame, view.loading_frame)

                view.ingredients_and_instructions.animate()
                        .alpha(1f)
                        .setDuration(250)

                val meal = details.get()

                view.ingredients.text = meal.ingredients()
                        .map { (ingredient, amount) ->
                            if (amount.isNullOrBlank()) {
                                ingredient
                            } else {
                                view.resources.getString(
                                        R.string.ingredient_with_amount,
                                        ingredient,
                                        amount
                                )
                            }
                        }
                        .joinToString("\n")

                // Many recipes become much more readable with an extra line break
                view.instructions.text = meal.strInstructions
                        .replace("\n", "\n\n")

                val tagsText = meal.tags().joinToString("  |  ")
                if (tagsText.isBlank()) {
                    hide(view.tags)
                } else {
                    show(view.tags)
                    view.tags.text = tagsText
                }
            }
            is Uninitialized, is Loading -> {
                show(view.loading_frame)
                hide(view.error_frame, view.ingredients_and_instructions)
                hide(view.tags)
            }
            is Fail -> {
                show(view.error_frame)
                hide(view.ingredients_and_instructions, view.loading_frame)
                hide(view.tags)
            }
        }
    }
}