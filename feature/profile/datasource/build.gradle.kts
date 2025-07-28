plugins {
    id("parkhang.library")
    id("parkhang.hilt")
}

android {
    namespace = "com.parkhang.mobile.feature.profile.datasource"
}

dependencies {
    implementation(projects.feature.profile.entity)

    implementation(projects.framework.network.client)

    implementation(libs.ktor.resources)
}
