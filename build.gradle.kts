// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.kotlin) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.lint) apply false
    alias(libs.plugins.gms.services) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    jacoco
    alias(libs.plugins.compose.compiler) apply false
}

tasks {
    val clean by registering(Delete::class) {
        delete(layout.buildDirectory)
    }
}

allprojects {
    apply(plugin = "org.jmailen.kotlinter")
}