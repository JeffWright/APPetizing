package com.jtw.appetizing.dagger

import android.content.Context
import com.jtw.appetizing.AppetizingApplication
import com.jtw.appetizing.BuildConfig
import com.jtw.appetizing.network.FlakyMealDbService
import com.jtw.appetizing.network.MealDbRetrofitService
import dagger.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Component(modules = [ApplicationModule::class])
@Singleton
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: AppetizingApplication): Builder

        fun build(): ApplicationComponent
    }

    fun newMainActivityComponentBuilder(): MainActivityComponent.Builder
}

@Module(
        subcomponents = [MainActivityComponent::class]
)
class ApplicationModule {

    companion object {
        private const val OK_HTTP_DISK_CACHE_SIZE_MB = 4
        private const val ONE_MB_IN_BYTES = 1024 * 1024
    }

    @Provides
    @Singleton
    fun provideContext(application: AppetizingApplication): Context = application

    @Provides
    @Singleton
    fun provideOkHttpClient(context: Context): OkHttpClient {
        val cacheDir = context.getDir("ok_http_cache", Context.MODE_PRIVATE)
        val cacheSizeInBytes = (OK_HTTP_DISK_CACHE_SIZE_MB * ONE_MB_IN_BYTES).toLong()

        return OkHttpClient.Builder()
                .cache(Cache(cacheDir, cacheSizeInBytes))
                .build()
    }

    @Provides
    @Singleton
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

        val retrofitService = retrofit.create(MealDbRetrofitService::class.java)

        @Suppress("ConstantConditionIf")
        return if (BuildConfig.SIMULATE_POOR_NETWORK) {
            FlakyMealDbService(retrofitService)
        } else {
            retrofitService
        }
    }
}

