import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidHiltPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply("com.google.devtools.ksp")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val hiltAndroid = libs.findLibrary("hilt-android").get()
            val hiltCompiler = libs.findLibrary("hilt-compiler").get()
            val hiltWorker = libs.findLibrary("hilt-worker").get()
            val hiltWorkerCompiler = libs.findLibrary("hilt-worker-compiler").get()
            dependencies {
                add("implementation", hiltAndroid)
                add("implementation", hiltWorker)
                add("ksp", hiltCompiler)
                add("ksp", hiltWorkerCompiler)
            }
        }
    }
}
