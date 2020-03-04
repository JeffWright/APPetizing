
buildscript {

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${com.jtw.appetizing.buildsrc.gradle_android_version}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${com.jtw.appetizing.buildsrc.Kotlin.version}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}