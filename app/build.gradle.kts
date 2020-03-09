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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
            isPseudoLocalesEnabled = true

            val simulate_poor_network: String by project

            buildConfigField("boolean", "SIMULATE_POOR_NETWORK", simulate_poor_network)
        }
        val release by getting {
            signingConfig = signingConfigs.findByName("debug")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), File("proguard-rules.pro"))

            buildConfigField("boolean", "SIMULATE_POOR_NETWORK", "false")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}


dependencies {
    implementation(Kotlin.stdlib)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.ktxCore)
    implementation(AndroidX.ktxActivity)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.recyclerView)

    implementation(RxJava.rxJava)
    implementation(RxJava.rxRelay)
    implementation(RxJava.rxBinding)
    implementation(RxJava.rxBindingRecyclerView)

    implementation(Retrofit.retrofit)
    implementation(Retrofit.rxAdapter)
    implementation(Retrofit.moshiConverter)

    implementation(Glide.glide)
    kapt(Glide.annotationProcessor)

    implementation(Dagger.dagger)
    kapt(Dagger.daggerCompiler)

    testImplementation(JUnit)
    testImplementation(Mockk)

    androidTestImplementation(JUnitForAndroidX)
    androidTestImplementation(AndroidXTestRules)
    androidTestImplementation(Espresso)

}

// Disable compiler warnings for inline classes, which are experimental
tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>> {
    kotlinOptions {
        freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
    }
}
