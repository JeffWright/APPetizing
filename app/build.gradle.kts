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

    buildTypes {
        val release by getting {
            isMinifyEnabled = false
            // TODO JTW
            //  proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
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

    implementation(RxJava.rxJava)
    implementation(RxJava.rxBinding)
    implementation(RxJava.rxPreferences)

    implementation(Retrofit.retrofit)
    implementation(Retrofit.rxAdapter)

    implementation(Dagger.dagger)
    kapt(Dagger.daggerCompiler)

    testImplementation(JUnit)

    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

}