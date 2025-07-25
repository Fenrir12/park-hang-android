plugins {
    id("parkhang.base")
    id("parkhang.compose")
    id("parkhang.composeCompiler")
    id("parkhang.core")
}

android {
    namespace = "com.parkhang.mobile.core.designsystem.component"
}
dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.common)

    implementation(libs.androidx.activity.compose)

    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.tooling.preview.android)

    debugApi(libs.androidx.compose.ui.tooling)
}
