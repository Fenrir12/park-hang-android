import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidFeatureView : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            with(pluginManager) {
                apply("parkhang.library")
                apply("parkhang.compose")
                apply("parkhang.hilt")
                apply("parkhang.composeCompiler")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                "implementation"(libs.findLibrary("androidx-hilt-navigation-compose").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-compose").get())
                "implementation"(libs.findLibrary("androidx-navigation-compose").get())

                "api"(libs.findLibrary("androidx-compose-material3").get())
                
                "debugApi"(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
                "debugApi"(libs.findLibrary("androidx-compose-ui-tooling").get())

                "implementation"(project(mapOf("path" to ":core:designsystem")))
            }
        }
    }
}
