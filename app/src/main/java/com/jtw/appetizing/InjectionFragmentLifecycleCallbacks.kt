package com.jtw.appetizing

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.jtw.appetizing.dagger.DaggerFragment
import com.jtw.appetizing.dagger.MainActivityComponent

class InjectionFragmentLifecycleCallbacks(
    private val daggerComponent: MainActivityComponent
) : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        super.onFragmentPreAttached(fm, f, context)
        Log.d(TAG, "onFragmentPreAttached")
        (f as? DaggerFragment)?.let {
            check(!it.isInjected)
            it.isInjected = true
            it.inject(daggerComponent)
        }
    }

}
