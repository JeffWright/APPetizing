import com.jtw.appetizing.buildsrc.*

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("org.jetbrains.kotlin.kapt")

}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.jtw.appetizing"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"

        // testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        val debug by getting {
            keyAlias = "debug"
            keyPassword = "android"
            storeFile = file("../debug.jks")
            storePassword = "android"
        }
    }

    buildTypes {
        val debug by getting {
            signingConfig = signingConfigs.findByName("debug")
            isMinifyEnabled = false
        }
        val release by getting {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), File("proguard-rules.pro"))
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {
    implementation(Kotlin.stdlib)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.ktxCore)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.recyclerView)

    implementation(RxJava.rxJava)
    implementation(RxJava.rxPreferences)
    implementation(RxJava.rxRelay)
    implementation(RxJava.rxBinding)
    implementation(RxJava.rxBindingRecyclerView)

    implementation(Retrofit.retrofit)
    implementation(Retrofit.rxAdapter)
    implementation(Retrofit.moshiConverter)

    implementation(Dagger.dagger)
    kapt(Dagger.daggerCompiler)

    testImplementation(JUnit)

    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

}