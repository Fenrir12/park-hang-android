plugins {
    id("parkhang.library")
    id("parkhang.hilt")
}

android {
    namespace = "com.touchtunes.parkhang.mobile.framework.geolocation"
}

dependencies {
    implementation(libs.play.services.location)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit4)
}
