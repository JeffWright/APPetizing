package com.jtw.appetizing.core

import com.jtw.appetizing.dagger.ApplicationScoped
import com.jtw.appetizing.network.MealDbService
import com.jtw.appetizing.util.plusAssign
import javax.inject.Inject

@ApplicationScoped
class AppetizingModelStore(
        initialState: AppState,
        private val mealDbService: MealDbService
) : BaseModelStore<AppState>(initialState) {

    class Factory @Inject constructor(
            private val mealDbService: MealDbService
    ) {
        fun get(initialState: AppState) = AppetizingModelStore(
                initialState,
                mealDbService
        )
    }


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
            is RetryCategoryEvent -> {
                val category = (previousState.chosenCategory as? ChosenCategory.Actual)?.category
                        ?: return Next.noChange()
                return Next.effect(LoadMealsEffect(category))
            }

            is LoadedMealsForCategoryEvent -> {
                return Next(previousState.copy(
                        chosenCategory = (previousState.chosenCategory as? ChosenCategory.Actual)
                                ?.copy(mealsInCategory = event.result)
                                ?: ChosenCategory.None
                ))
            }
            is ChoseMealEvent -> {
                return Next(
                        state = previousState.copy(
                                chosenMeal = ChosenMeal(
                                        event.mealId,
                                        event.mealName,
                                        event.imageUrl
                                )
                        ),
                        effects = listOf(
                                LoadMealDetailsEffect(event.mealId),
                                ShowMealDetailsEffect(event.sharedElementViewText, event.sharedElementViewImage))
                )
            }
            is LoadedMealDetailsEvent -> {
                return Next(previousState.copy(
                        chosenMeal = previousState.chosenMeal
                                ?.copy(mealDetails = event.result)
                ))
            }
            is RetryMealEvent -> {
                val mealId = previousState.chosenMeal?.mealId ?: return Next.noChange()
                return Next.effect(LoadMealDetailsEffect(mealId))
            }
        }

        return Next.noChange()
    }

}


