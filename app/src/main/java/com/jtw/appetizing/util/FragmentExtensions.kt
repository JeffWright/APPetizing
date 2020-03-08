package com.jtw.appetizing.util

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


inline fun FragmentManager.transaction(
        backstack: Boolean = true,
        action: FragmentTransaction.() -> Unit
) {
    beginTransaction().apply {
        action()
        if (backstack) {
            addToBackStack(null)
        }
    }.commit()
}