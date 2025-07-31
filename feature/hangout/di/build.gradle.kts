plugins {
    id("parkhang.library")
    id("parkhang.hilt")
}

android {
    namespace = "com.parkhang.mobile.feature.hangout.di"
}

dependencies {
    implementation(projects.feature.hangout.entity)
    implementation(projects.feature.hangout.datasource)

    implementation(projects.core.common)
    implementation(projects.core.checkin)

    implementation(projects.framework.geolocation)

    implementation(libs.ktor.resources)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
