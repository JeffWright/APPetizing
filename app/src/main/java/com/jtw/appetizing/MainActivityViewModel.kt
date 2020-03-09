package com.jtw.appetizing

import androidx.lifecycle.ViewModel
import com.jtw.appetizing.core.AppetizingModelStore

/** Just a simple way to persist the model store across rotations */
class MainActivityViewModel : ViewModel() {
    var modelStore: AppetizingModelStore? = null
}