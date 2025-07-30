plugins {
    id("parkhang.library")
    id("parkhang.hilt")
}

android {
    namespace = "com.touchtunes.parkhang.mobile.framework.persistence.datasource.checkinpreferences"
}

dependencies {
    implementation(projects.framework.persistence.localdatastorage)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit4)
}
