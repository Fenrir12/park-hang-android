plugins {
    id("parkhang.application")
    id("parkhang.compose")
    id("parkhang.hilt")
    id("parkhang.common")
    id("parkhang.composeCompiler")
}

android {
    namespace = "com.parkhang.mobile"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.parkhang.mobile"
        minSdk = libs.versions.minSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("${rootProject.projectDir}/app/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".dev"
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    packaging {
        resources {
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {
    //region feature modules
    implementation(projects.feature.parks.view)
    //endregion

    //region core modules
    implementation(projects.core.designsystem)
    //endregion

    //region framework modules
    implementation(projects.framework.network.client)
    //endregion

    //region third-party libraries
    implementation(libs.ktor.core)
    implementation(libs.ktor.okhttp)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.process)
    //endregion

    //region test dependencies
    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    //endregion

    //region androidTest dependencies
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //endregion
}