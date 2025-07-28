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

    implementation(projects.framework.geolocation)

    implementation(libs.ktor.resources)
    implementation(libs.maps.compose)
    implementation(libs.maps.compose.utils)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
