plugins {
    id("parkhang.library")
    id("parkhang.hilt")
}

android {
    namespace = "com.parkhang.mobile.core.userprofile"
}

dependencies {
    implementation(projects.framework.network.client)

    implementation(libs.ktor.resources)
}
