// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false  // For Android application projects
    alias(libs.plugins.kotlin.android) apply false       // For Kotlin Android projects
    alias(libs.plugins.kotlin.compose) apply false       // For Jetpack Compose support
    alias(libs.plugins.hiltAndroid) apply false          // For Dagger Hilt dependency injection
    alias(libs.plugins.kotlinAndroidKsp) apply false     // For Kotlin Symbol Processing (KSP)
    id("com.google.gms.google-services") version "4.4.2" apply false


}