plugins {
    id("parkhang.library")
    id("parkhang.core")
    id("parkhang.hilt")
}

android {
    namespace = "com.parkhang.mobile.framework.authentication"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.event)

    implementation(projects.framework.persistence.datasource.usercredentialspreferences)
    implementation(projects.framework.network.client)

    implementation(libs.auth0.jwt)
    implementation(libs.ktor.resources)
    implementation(libs.ktor.json)
}
