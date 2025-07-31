plugins {
    id("parkhang.feature.view")
}

android {
    namespace = "com.parkhang.mobile.feature.hangout.view"
}

dependencies {
    implementation(projects.feature.hangout.entity)
    implementation(projects.feature.hangout.di)

    implementation(projects.core.designsystem)
    implementation(projects.core.designsystem.components)
    implementation(projects.core.checkin)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
