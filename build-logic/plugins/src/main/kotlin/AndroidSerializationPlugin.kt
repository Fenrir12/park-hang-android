import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidSerializationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val kotlinXSerialization = libs.findLibrary("kotlinx-serialization").get()

            dependencies {
                add("implementation", kotlinXSerialization)
            }
        }
    }
}
