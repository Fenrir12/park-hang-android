plugins {
    id("parkhang.feature.view")
}

android {
    namespace = "com.parkhang.mobile.feature.parks.view"
}

dependencies {
    implementation(projects.feature.parks.entity)
    implementation(projects.feature.parks.di)

    implementation(projects.core.designsystem)
    implementation(projects.core.checkin)

    implementation(libs.maps.compose)
    implementation(libs.accompanist.permissions)
    implementation(libs.maps.compose.utils)
    implementation(libs.play.services.maps)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
