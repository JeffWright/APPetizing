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

    logger.invoke(msg)
}

var logger: (String) -> Unit = { Log.d(TAG, it) }