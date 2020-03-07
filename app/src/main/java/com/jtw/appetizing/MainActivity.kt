package com.jtw.appetizing

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.jtw.appetizing.core.*
import com.jtw.appetizing.dagger.DaggerMainActivityComponent
import com.jtw.appetizing.dagger.InjectionFragmentLifecycleCallbacks
import com.jtw.appetizing.feature.categories.CategoriesListFragment
import com.jtw.appetizing.feature.mealdetails.MealDetailsFragment
import com.jtw.appetizing.network.Uninitialized
import com.jtw.appetizing.util.log
import com.jtw.appetizing.util.plusAssign
import com.jtw.appetizing.util.replaceFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_container.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var modelStore: AppetizingModelStore

    private val daggerComponent = DaggerMainActivityComponent
            .builder()
            .activity(this)
            .build()

    private val fragmentLifecycleCallbacks = InjectionFragmentLifecycleCallbacks(daggerComponent)
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("JTW", "onCreate: ")
        supportFragmentManager.registerFragmentLifecycleCallbacks(
                fragmentLifecycleCallbacks,
                true
        )

        super.onCreate(savedInstanceState)

        daggerComponent.inject(this)

        setContentView(R.layout.fragment_container)

        val viewModel: MainActivityViewModel by viewModels()

        if (savedInstanceState == null) {
            replaceFragment(CategoriesListFragment(), container_primary)

            modelStore.state.accept(AppState(
                    categories = Uninitialized,
                    chosenCategory = ChosenCategory.None
            ))

            modelStore.onEvent(RequestLoadCategoriesEvent)
        } else {
            log("restore")
            viewModel.model?.let {
                modelStore.state.accept(it)
            }

            ///////////
            supportFragmentManager.findFragmentByTag("DETAILS")
                    ?.let { detailFragment ->
                        supportFragmentManager.beginTransaction()
                                .remove(detailFragment)
                                .commit()
                       
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.container_primary, MealDetailsFragment())

                                .setCustomAnimations(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_left,
                                        R.anim.slide_in_left,
                                        R.anim.slide_out_right
                                )

                                .addToBackStack(null)
                                .commit()
                    }
            ///////////

        }

        disposable += modelStore.state.subscribe {
            viewModel.model = it
        }


        navigator.bind(container_primary)
    }

}

class MainActivityViewModel : ViewModel() {
    var model: AppState? = null
}