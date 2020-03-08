package com.jtw.appetizing.feature.mealdetails

import com.jtw.appetizing.core.AppState
import com.jtw.appetizing.core.ChosenMeal
import com.jtw.appetizing.core.Presenter
import javax.inject.Inject

class MealDetailsPresenter @Inject constructor(
        override val renderedView: MealDetailsView
) : Presenter<ChosenMeal>() {
    override fun AppState.mapToModel() = chosenMeal
}