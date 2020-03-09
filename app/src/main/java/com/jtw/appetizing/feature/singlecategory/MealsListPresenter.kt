package com.jtw.appetizing.feature.singlecategory

import com.jtw.appetizing.core.AppState
import com.jtw.appetizing.core.ChosenCategory
import com.jtw.appetizing.core.Presenter
import javax.inject.Inject

class MealsListPresenter @Inject constructor(
        override val renderedView: MealsListView
) : Presenter<ChosenCategory.Actual>() {

    override fun AppState.mapToModel() = chosenCategory as? ChosenCategory.Actual

}
