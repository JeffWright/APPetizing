package com.jtw.appetizing.list

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jtw.appetizing.dagger.ApplicationScoped
import com.jtw.appetizing.network.*
import com.jtw.appetizing.plusAssign
import com.jtw.util.log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@ApplicationScoped
class ModelStore @Inject constructor(
        private val mealDbService: MealDbService
) {
    val state: BehaviorRelay<AppState> = BehaviorRelay.create() // TODO JTW private
    private val events: BehaviorRelay<Any> = BehaviorRelay.create()
    val disposable = CompositeDisposable()

    init {
        disposable += Observable.zip<AppState, Any, AppState>(
                        state.observeOn(Schedulers.single()), // TODO JTW
                        events,
                        BiFunction { previousState, event -> reduce(previousState, event) }
                )
                .subscribe(state)

        disposable += state.subscribe { state ->
            log("State output: $state ")
        }
        disposable += events.subscribe { event ->
            log("Event input: $event")
        }
    }

    fun onEvent(event: Any) = events.accept(event)

    private fun reduce(previousState: AppState, event: Any): AppState {
        when (event) {
            is RequestLoadCategoriesEvent -> {
                disposable += mealDbService.getCategories()
                        .subscribe { onEvent(LoadedCategoriesEvent(it)) }
            }
            is LoadedCategoriesEvent -> {
                return previousState.copy(categories = event.result)
            }
            is ChoseCategoryEvent -> {
                disposable += mealDbService
                        .getMealsForCategory(event.category)
                        .subscribe { onEvent(LoadedMealsForCategoryEvent(it)) }
                return previousState.copy(
                        chosenCategory = ChosenCategory.Actual(
                                category = event.category
                        )
                )
            }
            is LoadedMealsForCategoryEvent -> {
                return previousState.copy(
                        chosenCategory = (previousState.chosenCategory as? ChosenCategory.Actual)
                                ?.copy(mealsInCategory = event.result)
                                ?: ChosenCategory.None
                )
            }
        }

        return previousState
    }

}


object RequestLoadCategoriesEvent
data class LoadedCategoriesEvent(val result: Async<List<String>>)

data class ChoseCategoryEvent(val category: String)
data class LoadedMealsForCategoryEvent(val result: Async<List<MealWithThumb>>)

data class AppState(
        val categories: Async<List<String>>,
        val chosenCategory: ChosenCategory
)

sealed class ChosenCategory {

    data class Actual(
            val category: String,
            val mealsInCategory: Async<List<MealWithThumb>> = Uninitialized,
            val chosenMeal: ChosenMeal? = null
    ) : ChosenCategory()

    object None : ChosenCategory()
}

data class ChosenMeal(
        val mealId: String,
        val mealDetails: MealDetails?
)
