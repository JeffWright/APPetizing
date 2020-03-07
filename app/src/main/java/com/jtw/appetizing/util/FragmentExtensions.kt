package com.jtw.appetizing.util

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


fun FragmentManager.replaceFragment(
        fragment: Fragment,
        @IdRes containerId: Int,
        tag: String? = null
) {
    transaction {
        replace(containerId, fragment, tag)
    }
}

fun AppCompatActivity.replaceFragment(
        fragment: Fragment,
        container: ViewGroup,
        tag: String? = null
) {
    supportFragmentManager.transaction {
        replace(container.id, fragment, tag)
    }
}


fun FragmentManager.replaceFragment(
        fragment: Fragment,
        container: ViewGroup,
        tag: String? = null
) {
    transaction {
        replace(container.id, fragment, tag)
    }
}

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