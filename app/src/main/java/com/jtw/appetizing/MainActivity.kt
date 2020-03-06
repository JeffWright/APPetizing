package com.jtw.appetizing

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jtw.appetizing.dagger.DaggerMainActivityComponent
import com.jtw.appetizing.dagger.InjectionFragmentLifecycleCallbacks
import com.jtw.appetizing.feature.categories.CategoriesListFragment
import com.jtw.appetizing.util.replaceFragment
import kotlinx.android.synthetic.main.fragment_container.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var navigator: Navigator

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

        setContentView(R.layout.fragment_container)
        replaceFragment(CategoriesListFragment(), container)

        daggerComponent.inject(this)

        navigator.bind(container)
    }

}