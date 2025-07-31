plugins {
    id("parkhang.library")
    id("parkhang.hilt")
}

android {
    namespace = "com.parkhang.mobile.feature.hangout.datasource"
}

dependencies {
    implementation(projects.feature.hangout.entity)

    implementation(projects.framework.network.client)

    implementation(libs.ktor.resources)
}
