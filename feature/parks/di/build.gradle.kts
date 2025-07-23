plugins {
    id("parkhang.library")
    id("parkhang.hilt")
}

android {
    namespace = "com.parkhang.mobile.feature.parks.di"
}

dependencies {
    implementation(projects.feature.parks.entity)
    implementation(projects.feature.parks.datasource)

    implementation(projects.core.common)

    implementation(libs.ktor.resources)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
