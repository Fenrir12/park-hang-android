plugins {
    id("parkhang.feature.view")
}

android {
    namespace = "com.parkhang.mobile.feature.profile.view"
}

dependencies {
    implementation(projects.feature.profile.entity)
    implementation(projects.feature.profile.di)

    implementation(projects.core.userprofile)
    implementation(projects.core.designsystem)
    implementation(projects.core.designsystem.components)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
