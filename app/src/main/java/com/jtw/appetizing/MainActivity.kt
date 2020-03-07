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
            replaceFragment(CategoriesListFragment(), container)

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
        }

        disposable += modelStore.state.subscribe {
            viewModel.model = it
        }


        navigator.bind(container)
    }

}

class MainActivityViewModel : ViewModel() {
    var model: AppState? = null
}