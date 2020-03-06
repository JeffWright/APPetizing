package com.jtw.appetizing.dagger

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.jtw.appetizing.MainActivity
import com.jtw.appetizing.feature.categories.CategoriesListFragment
import com.jtw.appetizing.feature.mealdetails.MealDetailsFragment
import com.jtw.appetizing.feature.singlecategory.MealsListFragment
import com.jtw.appetizing.network.MealDbRetrofitService
import dagger.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Scope


@Component(modules = [ActivityModule::class])
@ApplicationScoped
interface MainActivityComponent {
    @Component.Builder
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
class ActivityModule {

    companion object {
        private const val OK_HTTP_DISK_CACHE_SIZE_MB = 4
        private const val ONE_MB_IN_BYTES = 1024 * 1024
    }

    @Provides
    fun provideFragmentManager(activity: AppCompatActivity): FragmentManager {
        return activity.supportFragmentManager
    }

    @Provides
    fun provideContext(activity: AppCompatActivity): Context = activity

    @Provides
    fun provideOkHttpClient(context: Context): OkHttpClient {
        val cacheDir = context.getDir("ok_http_cache", Context.MODE_PRIVATE)
        val cacheSizeInBytes = (OK_HTTP_DISK_CACHE_SIZE_MB * ONE_MB_IN_BYTES).toLong()

        return OkHttpClient.Builder()
                .cache(Cache(cacheDir, cacheSizeInBytes))
                .build()
    }

    @Provides
    fun provideMealDbService(okHttpClient: OkHttpClient): MealDbRetrofitService {

        val client = okHttpClient.newBuilder()
                .build()


        val retrofit = Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.createAsync()
                )
                .addConverterFactory(
                        MoshiConverterFactory.create()
                )
                .baseUrl(MealDbRetrofitService.BASE_URL)
                .build()

        return retrofit.create(MealDbRetrofitService::class.java)
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScoped