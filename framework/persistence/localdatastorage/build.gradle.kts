import com.google.protobuf.gradle.GenerateProtoTask

plugins {
    id("parkhang.library")
    id("parkhang.hilt")
    alias(libs.plugins.google.protobuf)
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
    namespace = "com.parkhang.mobile.framework.persistence.localdatastorage"
}

dependencies {
    implementation(libs.datastore)
    api(libs.datastore.core)
    api(libs.google.protobuf.kotlin)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit4)
}

protobuf {
    protoc {
        artifact = libs.google.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                val java by registering {
                    option("lite")
                }
                val kotlin by registering {
                    option("lite")
                }
            }
        }
    }
}

androidComponents {
    onVariants(selector().all()) { variant ->
        afterEvaluate {
            val protoTask =
                project.tasks.getByName("generate" + variant.name.replaceFirstChar { it.uppercaseChar() } + "Proto") as GenerateProtoTask

            project.tasks.getByName("ksp" + variant.name.replaceFirstChar { it.uppercaseChar() } + "Kotlin") {
                dependsOn(protoTask)
                (this as org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool<*>).setSource(
                    protoTask.outputBaseDir
                )
            }
        }
    }
}
