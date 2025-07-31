plugins {
    id("parkhang.library")
    id("parkhang.hilt")
}

android {
    namespace = "com.parkhang.mobile.core.checkin"
}

dependencies {
    implementation(projects.core.event)

    implementation(projects.framework.network.client)
    implementation(projects.framework.persistence.datasource.checkinpreferences)
    implementation(projects.framework.authentication)

    implementation(libs.ktor.resources)
}
