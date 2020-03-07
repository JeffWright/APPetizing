package com.jtw.appetizing.core

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jtw.appetizing.dagger.ApplicationScoped
import com.jtw.appetizing.network.MealDbService
import com.jtw.appetizing.util.log
import com.jtw.appetizing.util.plusAssign
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface ModelStore {
    val state: BehaviorRelay<AppState>

    fun onEvent(event: Event)
}

abstract class BaseModelStore : ModelStore {
    override val state: BehaviorRelay<AppState> = BehaviorRelay.create() // TODO JTW private
    val events: PublishRelay<Event> = PublishRelay.create()
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

        disposable += effects.subscribe(::handleEffect)

        disposable += state.subscribe { state ->
            log("State output: $state ")
        }
        disposable += events.subscribe { event ->
            log("Event input: $event")
        }
    }

    override fun onEvent(event: Event) = events.accept(event)
    fun onEffect(event: Effect) = effects.accept(event)

    abstract fun reduce(previousState: AppState, event: Event): Next<AppState>

    abstract fun handleEffect(effect: Effect)
}

@ApplicationScoped
class AppetizingModelStore @Inject constructor(
        private val mealDbService: MealDbService
) : BaseModelStore() {

    override fun handleEffect(effect: Effect) {
        when (effect) {
            is LoadCategoriesEffect -> {
                disposable += mealDbService.getCategories()
                        .subscribe { onEvent(LoadedCategoriesEvent(it)) }
            }
            is LoadMealsEffect -> {
                disposable += mealDbService
                        .getMealsForCategory(effect.category)
                        .subscribe { onEvent(LoadedMealsForCategoryEvent(it)) }
            }
            is LoadMealDetailsEffect -> {
                disposable += mealDbService
                        .getMealDetails(effect.mealId)
                        .subscribe { onEvent(LoadedMealDetailsEvent(it)) }
            }
        }
    }

    override fun reduce(previousState: AppState, event: Event): Next<AppState> {
        when (event) {
            is RequestLoadCategoriesEvent -> {
                return Next.effect(LoadCategoriesEffect)
            }
            is LoadedCategoriesEvent -> {
                return Next(
                        previousState.copy(categories = event.result)
                )
            }
            is ChoseCategoryEvent -> {
                return Next(
                        state = previousState.copy(
                                chosenCategory = ChosenCategory.Actual(category = event.category)
                        ),
                        effects = listOf(LoadMealsEffect(event.category), ShowMealsListEffect)
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
                if (previousState.chosenMeal?.mealId == event.mealId) {
                    return Next.noChange()
                }
               
                return Next(
                        state = previousState.copy(
                                chosenMeal = ChosenMeal(
                                        event.mealId,
                                        event.mealName,
                                        event.imageUrl
                                )
                        ),
                        effects = listOf(LoadMealDetailsEffect(event.mealId), ShowMealDetailsEffect)
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


