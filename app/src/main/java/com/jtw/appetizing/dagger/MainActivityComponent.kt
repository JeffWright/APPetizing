package com.jtw.appetizing.dagger

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.jtw.appetizing.MainActivity
import com.jtw.appetizing.feature.categories.CategoriesListFragment
import com.jtw.appetizing.feature.mealdetails.MealDetailsFragment
import com.jtw.appetizing.feature.singlecategory.MealsListFragment
import dagger.*


@Subcomponent(modules = [MainActivityModule::class])
@ActivityScoped
interface MainActivityComponent {
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun activity(appCompatActivity: AppCompatActivity): Builder

        fun build(): MainActivityComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(categoriesListFragment: CategoriesListFragment)
    fun inject(mealsListFragment: MealsListFragment)
    fun inject(mealDetailsFragment: MealDetailsFragment)
}

@Module
class MainActivityModule {

    @Provides
    fun provideFragmentManager(activity: AppCompatActivity): FragmentManager {
        return activity.supportFragmentManager
    }

    @Provides
    fun provideContext(activity: AppCompatActivity): Context = activity

}

