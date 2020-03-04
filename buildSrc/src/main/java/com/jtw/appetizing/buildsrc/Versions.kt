package com.jtw.appetizing.buildsrc

const val gradle_android_version = "4.0.0-beta01"

object Kotlin {
    const val version = "1.3.70"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
}

object AndroidX {
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
    const val appCompat = "androidx.appcompat:appcompat:1.1.0"
    const val ktxCore = "androidx.core:core-ktx:1.2.0"
}

object RxJava {
    const val rxJava = "io.reactivex.rxjava2:rxjava:2.2.18"
    const val rxBinding ="com.jakewharton.rxbinding2:rxbinding:2.2.0"
    const val rxPreferences = "com.f2prateek.rx.preferences2:rx-preferences:2.0.0"
}

object Retrofit {
    private const val version = "2.7.2"

    const val retrofit = "com.squareup.retrofit2:retrofit:$version"
    /** https://github.com/square/retrofit/tree/master/retrofit-adapters/rxjava2 */
    const val rxAdapter = "com.squareup.retrofit2:adapter-rxjava2:$version"
}

object Dagger {
    private const val version = "2.26"
    const val dagger = "com.google.dagger:dagger:$version"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:$version"
}

const val JUnit = "junit:junit:4.12"
