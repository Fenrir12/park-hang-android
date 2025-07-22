import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidCorePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            with(pluginManager) {
                apply("parkhang.base")
                apply("parkhang.compose")
                apply("parkhang.hilt")
                apply("jacoco")
                apply("parkhang.composeCompiler")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                "implementation"(libs.findLibrary("androidx-hilt-navigation-compose").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-compose").get())
                "implementation"(libs.findLibrary("androidx-navigation-compose").get())

                "api"(libs.findLibrary("androidx-compose-material3").get())
                "api"(libs.findLibrary("androidx-compose-runtime").get())
                "api"(libs.findLibrary("androidx-compose-ui-tooling-preview-android").get())

                "debugApi"(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
                "debugApi"(libs.findLibrary("androidx-compose-ui-tooling").get())
            }
        }
    }
}
