plugins {
    id("parkhang.library")
    id("parkhang.hilt")
}

android {
    namespace = "com.parkhang.mobile.feature.parks.entity"
}

dependencies {
    implementation(libs.maps.compose)
    implementation(libs.maps.compose.utils)
}
