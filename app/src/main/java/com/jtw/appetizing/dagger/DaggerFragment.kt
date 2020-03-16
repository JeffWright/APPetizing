package com.jtw.appetizing.dagger

import android.content.Context
import androidx.fragment.app.Fragment
import com.jtw.appetizing.MainActivity

abstract class DaggerFragment : Fragment() {
    private var isInjected: Boolean = false

    abstract fun inject(component: MainActivityComponent)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (isInjected) {
            return
        }
        isInjected = true

        val component = (activity as MainActivity).daggerComponent
        inject(component)
    }
}
