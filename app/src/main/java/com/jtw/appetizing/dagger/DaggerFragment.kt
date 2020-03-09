package com.jtw.appetizing.dagger

import androidx.fragment.app.Fragment

abstract class DaggerFragment : Fragment() {
    var isInjected: Boolean = false

    abstract fun inject(component: MainActivityComponent)
}
