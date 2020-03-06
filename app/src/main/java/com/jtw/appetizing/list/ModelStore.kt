package com.jtw.appetizing.list

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jtw.appetizing.ShowMealDetailsEffect
import com.jtw.appetizing.ShowMealsListEffect
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
    private val events: PublishRelay<Event> = PublishRelay.create()
    val effects: PublishRelay<Effect> = PublishRelay.create()
    val disposable = CompositeDisposable()

    init {
        disposable += Observable.zip<AppState, Event, Next<AppState>>(
                        state.observeOn(Schedulers.single()), // TODO JTW
                        events,
                        BiFunction { previousState, event -> reduce(previousState, event) }
                )
                .subscribe { next ->
                    next.effects.forEach { effects.accept(it) }
                    (next.state ?: state.value)?.let {
                        state.accept(it)
                    }
                }

        disposable += state.subscribe { state ->
            log("State output: $state ")
        }
        disposable += events.subscribe { event ->
            log("Event input: $event")
        }
    }

    fun onEvent(event: Event) = events.accept(event)
    fun onEffect(event: Effect) = effects.accept(event)

    private fun reduce(previousState: AppState, event: Event): Next<AppState> {
        when (event) {
            is RequestLoadCategoriesEvent -> {
                disposable += mealDbService.getCategories() // TODO effect
                        .subscribe { onEvent(LoadedCategoriesEvent(it)) }
                return Next.noChange()
            }
            is LoadedCategoriesEvent -> {
                return Next(
                        previousState.copy(categories = event.result)
                )
            }
            is ChoseCategoryEvent -> {
                disposable += mealDbService
                        .getMealsForCategory(event.category)
                        .subscribe { onEvent(LoadedMealsForCategoryEvent(it)) }
                return Next(
                        state = previousState.copy(
                                chosenCategory = ChosenCategory.Actual(category = event.category)
                        ),
                        effects = listOf(ShowMealsListEffect())
                )
            }
            is LoadedMealsForCategoryEvent -> {
                return Next(previousState.copy(
                        chosenCategory = (previousState.chosenCategory as? ChosenCategory.Actual)
                                ?.copy(mealsInCategory = event.result)
                                ?: ChosenCategory.None
                ))
            }
            is ChoseMealEvent -> {
                disposable += mealDbService
                        .getMealDetails(event.mealId)
                        .subscribe { onEvent(LoadedMealDetailsEvent(it)) }
                return Next(
                        state = previousState.copy(
                                chosenMeal = ChosenMeal(
                                        event.mealId,
                                        event.mealName,
                                        event.imageUrl
                                )
                        ),
                        effects = listOf(ShowMealDetailsEffect())
                )
            }
            is LoadedMealDetailsEvent -> {
                return Next(previousState.copy(
                        chosenMeal = previousState.chosenMeal
                                ?.copy(mealDetails = event.result)
                ))
            }
        }
        return Next.noChange()
    }

}

/** Inspired (from memory) by Spotify's Mobius library */
open class Next<STATE>(
        /** null means no change from previous state */
        val state: STATE? = null,
        val effects: List<Effect> = emptyList()
) {

    companion object {
        fun <STATE> effects(effects: List<Effect>) = Next<STATE>(effects = effects)

        // class effects<STATE>(effects: List<Effect>) = Next<STATE>(effects)
        //
        fun <STATE> noChange() = Next<STATE>(effects = emptyList())
    }
}

interface Effect
interface Event

object RequestLoadCategoriesEvent : Event
data class LoadedCategoriesEvent(val result: Async<List<String>>) : Event

data class ChoseCategoryEvent(val category: String) : Event
data class LoadedMealsForCategoryEvent(val result: Async<List<MealWithThumb>>) : Event

data class ChoseMealEvent(
        val mealId: String,
        /** Optimization so that the next page can show something while waiting for the network */
        val mealName: String,
        /** Optimization so that the next page can show something while waiting for the network */
        val imageUrl: String
) : Event

data class LoadedMealDetailsEvent(val result: Async<MealDetails>) : Event

data class AppState(
        val categories: Async<List<String>>,
        val chosenCategory: ChosenCategory = ChosenCategory.None,
        val chosenMeal: ChosenMeal? = null
)

inline class MealCategory(val string: String)

sealed class ChosenCategory {

    data class Actual(
            val category: String,
            val mealsInCategory: Async<List<MealWithThumb>> = Uninitialized
    ) : ChosenCategory()

    object None : ChosenCategory()
}

data class ChosenMeal(
        val mealId: String,
        val mealName: String,
        val imageUrl: String,
        val mealDetails: Async<MealDetails> = Uninitialized
)
