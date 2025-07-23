plugins {
    id("parkhang.library")
    id("parkhang.hilt")
}

android {
    namespace = "com.parkhang.mobile.feature.parks.datasource"
}

dependencies {
    implementation(projects.feature.parks.entity)

    implementation(projects.framework.network.client)

    implementation(libs.ktor.resources)
}
