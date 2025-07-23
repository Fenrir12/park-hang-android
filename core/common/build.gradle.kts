plugins {
    id("parkhang.base")
    id("parkhang.hilt")
    id("parkhang.compose")
    id("parkhang.composeCompiler")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.parkhang.mobile.core.common"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
