package com.jtw.appetizing.feature.mealdetails

import android.view.View
import com.bumptech.glide.Glide
import com.jtw.appetizing.core.ChosenMeal
import com.jtw.appetizing.core.RenderedView
import com.jtw.appetizing.network.*
import com.jtw.appetizing.network.pojo.ingredients
import kotlinx.android.synthetic.main.loading.view.*
import kotlinx.android.synthetic.main.meal_details.view.*
import javax.inject.Inject

class MealDetailsView @Inject constructor() : RenderedView<ChosenMeal> {
    override fun bind(view: View) {
    }

    override fun render(view: View, model: ChosenMeal) {
        view.meal_name.text = model.mealName

        Glide.with(view.image)
                .load(model.imageUrl)
                .into(view.image)

        when (val details = model.mealDetails) {
            is Success -> {
                val meal = details.get()

                view.ingredients_and_instructions.visibility = View.VISIBLE
                view.loading.visibility = View.GONE

                // view.ingredients_and_instructions.animate()
                //         .alpha(1f)
                //         .setDuration(250)
                // }

                view.ingredients.text = meal.ingredients()
                        .map { (ingredient, amount) ->
                            "$ingredient  --  $amount"
                        }
                        .joinToString("\n")

                view.instructions.text = meal.strInstructions
                        .replace("\n", "\n\n")


            }
            is Uninitialized, is Loading -> {
                view.ingredients_and_instructions.visibility = View.GONE
                view.loading.visibility = View.VISIBLE
            }
            is Fail -> {

            }
        }
    }
}