package com.jtw.appetizing

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jtw.appetizing.core.*
import com.jtw.appetizing.dagger.DaggerMainActivityComponent
import com.jtw.appetizing.dagger.InjectionFragmentLifecycleCallbacks
import com.jtw.appetizing.feature.categories.CategoriesListFragment
import com.jtw.appetizing.feature.mealdetails.MealDetailsFragment
import com.jtw.appetizing.network.Uninitialized
import com.jtw.appetizing.util.transaction
import kotlinx.android.synthetic.main.fragment_container.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var modelStoreFactory: AppetizingModelStore.Factory

    private val daggerComponent = DaggerMainActivityComponent
            .builder()
            .activity(this)
            .build()

    private val fragmentLifecycleCallbacks = InjectionFragmentLifecycleCallbacks(daggerComponent)

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
        val modelStore = viewModel.modelStore ?: modelStoreFactory.get(createDefaultState())
        viewModel.modelStore = modelStore

        val primaryContainerId = container_primary.id
        val secondaryContainerId = (container_secondary ?: container_primary).id
        val isTwoPane = container_secondary != null

        if (savedInstanceState == null) {
            supportFragmentManager.transaction(false) {
                replace(R.id.container_primary, CategoriesListFragment())
            }

            modelStore.onEvent(RequestLoadCategoriesEvent)
        } else {
            supportFragmentManager.findFragmentByTag(MealDetailsFragment.TAG)
                    ?.let { detailFragment ->
                        // There is a detail fragment active, but it's not where we want it

                        // So, first, remove it
                        supportFragmentManager.transaction(false) {
                            remove(detailFragment)
                        }

                        // Then put it in the right location
                        val containerIdForDetails = if (isTwoPane) {
                            supportFragmentManager.popBackStack()
                            secondaryContainerId
                        } else {
                            primaryContainerId
                        }

                        supportFragmentManager.transaction(!isTwoPane) {
                            replace(containerIdForDetails, MealDetailsFragment(), MealDetailsFragment.TAG)

                            setCustomAnimations(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left,
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_right
                            )
                        }
                    }

        }

        navigator.bind(
                modelStore,
                container_primary.id,
                secondaryContainerId
        )

    }

    override fun onPause() {
        super.onPause()
        navigator.unbind()
    }


    private fun createDefaultState(): AppState {
        return AppState(
                categories = Uninitialized,
                chosenCategory = ChosenCategory.None
        )
    }
}

