package com.jtw.appetizing.util

import android.util.Log
import com.jtw.appetizing.BuildConfig

const val TAG = "APPetizing/LOG"

@Suppress("UNUSED")
fun log(msg: String) {
    if (!BuildConfig.DEBUG) {
        // Only log in debug builds
        return
    }

    try {
        Log.d(TAG, msg)
    } catch (exc: Exception) {
        // Happens in unit tests, where Log.d isn't available
    }

}