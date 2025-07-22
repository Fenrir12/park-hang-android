import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidCommonPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("parkhang.serialization")
            }

            dependencies {
                add("implementation", project(mapOf("path" to ":core:common")))
            }
        }
    }
}
