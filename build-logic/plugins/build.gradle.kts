plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("parkhang.application") {
            id = name
            implementationClass = "AndroidApplicationPlugin"
        }
    }
    plugins {
        register("parkhang.base") {
            id = name
            implementationClass = "AndroidBasePlugin"
        }
    }
    plugins {
        register("parkhang.common") {
            id = name
            implementationClass = "AndroidCommonPlugin"
        }
    }
    plugins {
        register("parkhang.serialization") {
            id = name
            implementationClass = "AndroidSerializationPlugin"
        }
    }
    plugins {
        register("parkhang.library") {
            id = name
            implementationClass = "AndroidLibraryPlugin"
        }
    }
    plugins {
        register("parkhang.compose") {
            id = name
            implementationClass = "AndroidComposePlugin"
        }
    }
    plugins {
        register("parkhang.hilt") {
            id = name
            implementationClass = "AndroidHiltPlugin"
        }
    }

    plugins {
        register("parkhang.feature.view") {
            id = name
            implementationClass = "AndroidFeatureView"
        }
    }
    plugins {
        register("parkhang.core") {
            id = name
            implementationClass = "AndroidCorePlugin"
        }
    }

    plugins {
        register("parkhang.composeCompiler") {
            id = name
            implementationClass = "ComposeCompilerPlugin"
        }
    }
}
