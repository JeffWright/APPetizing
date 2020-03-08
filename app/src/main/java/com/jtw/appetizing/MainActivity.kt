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
import kotlinx.android.synthetic.main.fragment_container.container_primary
import kotlinx.android.synthetic.main.fragment_container_two_pane.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        const val BUNDLE_KEY_TWO_PANE = "two_pane"

        /** If the screen is wider than this, we will show the list and details side-by-side */
        const val TWO_PANE_WIDTH = 480
    }

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var modelStoreFactory: AppetizingModelStore.Factory

    private val viewModel: MainActivityViewModel by viewModels()
    private val modelStore by lazy {
        viewModel.modelStore ?: modelStoreFactory.get(createDefaultState())
    }

    private val primaryContainerId = R.id.container_primary
    private val secondaryContainerId
        get() = if (isTwoPane) R.id.container_secondary else R.id.container_primary
    private val isTwoPane by lazy { shouldBeTwoPane() }

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

        setContentView(if (isTwoPane) R.layout.fragment_container_two_pane else R.layout.fragment_container)

        viewModel.modelStore = modelStore

        if (savedInstanceState == null) {
            supportFragmentManager.transaction(false) {
                replace(R.id.container_primary, CategoriesListFragment())
            }

            modelStore.onEvent(RequestLoadCategoriesEvent)
        } else {

            if (savedInstanceState.getBoolean(BUNDLE_KEY_TWO_PANE) != isTwoPane) {

                // TODO JTW extract method
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

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(BUNDLE_KEY_TWO_PANE, isTwoPane)
    }

    override fun onStart() {
        super.onStart()
        val secondaryContainerId = (container_secondary ?: container_primary).id
        navigator.bind(
                modelStore,
                container_primary.id,
                secondaryContainerId
        )
    }

    override fun onStop() {
        super.onStop()
        navigator.unbind()
    }


    private fun createDefaultState(): AppState {
        return AppState(
                categories = Uninitialized,
                chosenCategory = ChosenCategory.None
        )
    }

    /**
     * Have to do this programmatically, because I couldn't find a combination of qualifiers that
     * gives the right result.  Using layout-land looks bad when split-screen, and sw480dp rules
     * out phones.
     */
    private fun shouldBeTwoPane(): Boolean {
        val configuration = resources.configuration
        return configuration.screenWidthDp >= TWO_PANE_WIDTH
    }
}

