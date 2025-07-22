import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.extensions.getByType<VersionCatalogsExtension>().named("libs")

        target.dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
        }

        target.plugins.withId("com.android.application") {
            val extension = target.extensions.getByType<ApplicationExtension>()
            configureAndroid(extension)
        }
        target.plugins.withId("com.android.library") {
            val extension = target.extensions.getByType<LibraryExtension>()
            configureAndroid(extension)
        }
    }

    private fun configureAndroid(
        extensions: CommonExtension<*, *, *, *, *, *>
    ) {
        extensions.apply {
            buildFeatures {
                compose = true
            }
        }
    }
}
