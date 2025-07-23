plugins {
    id("parkhang.library")
    id("parkhang.core")
    id("parkhang.hilt")
}

android {
    namespace = "com.parkhang.mobile.framework.network.client"
}

dependencies {
    //region third-party libraries
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.ktor.auth)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.content)
    implementation(libs.ktor.core)
    implementation(libs.ktor.json)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.encoding)
    implementation(libs.ktor.resources)
    //endregion

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
