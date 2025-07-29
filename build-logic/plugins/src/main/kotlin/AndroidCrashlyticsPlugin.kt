import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidCrashlyticsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val firebaseAnalytics = libs.findLibrary("firebase.analytics").get()
            val firebaseCrashlytics = libs.findLibrary("firebase.crashlytics").get()
            dependencies {
                add("implementation", firebaseAnalytics)
                add("implementation", firebaseCrashlytics)
            }
        }
    }
}
