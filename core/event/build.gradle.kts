plugins {
    id("parkhang.library")
    id("parkhang.hilt")
}

android {
    namespace = "com.parkhang.mobile.core.event"
}

dependencies {
    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
