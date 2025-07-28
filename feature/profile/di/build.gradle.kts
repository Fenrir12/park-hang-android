plugins {
    id("parkhang.library")
    id("parkhang.hilt")
}

android {
    namespace = "com.parkhang.mobile.feature.profile.di"
}

dependencies {
    implementation(projects.feature.profile.entity)

    implementation(projects.core.common)

    implementation(projects.framework.authentication)

    implementation(libs.ktor.resources)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
