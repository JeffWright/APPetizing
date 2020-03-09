package com.jtw.appetizing.util

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


inline fun FragmentManager.transaction(
        backstack: Boolean = true,
        block: FragmentTransaction.() -> Unit
) {
    beginTransaction().apply {
        block()
        if (backstack) {
            addToBackStack(null)
        }
    }.commit()
}