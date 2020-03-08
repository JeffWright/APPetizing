package com.jtw.appetizing.feature.categories

import com.jtw.appetizing.core.AppState
import com.jtw.appetizing.core.Presenter
import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.network.Async
import javax.inject.Inject

class CategoryListPresenter @Inject constructor(
        override val renderedView: CategoryListView
) : Presenter<Async<List<MealCategory>>>() {

    override fun AppState.mapToModel() = categories

}