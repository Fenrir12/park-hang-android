plugins {
    id("parkhang.library")
    id("parkhang.hilt")
}

android {
    namespace = "com.touchtunes.parkhang.mobile.persistence.datasource.usercredentialspreferences"
}

dependencies {
    implementation(projects.framework.persistence.localdatastorage)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit4)
}
