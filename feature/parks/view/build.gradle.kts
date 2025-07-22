plugins {
    id("parkhang.feature.view")

}

android {
    namespace = "com.parkhang.mobile.feature.parks.view"
}

dependencies {
    //region module libraries
    implementation(projects.feature.parks.entity)
    implementation(projects.feature.parks.di)
    implementation(projects.feature.parks.datasource)

    implementation(projects.core.designsystem)
    //endregion

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
