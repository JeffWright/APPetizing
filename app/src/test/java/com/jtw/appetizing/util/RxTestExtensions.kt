package com.jtw.appetizing.util

import io.reactivex.schedulers.Schedulers

fun synchronousScheduler() = Schedulers.from { cmd -> cmd.run() }