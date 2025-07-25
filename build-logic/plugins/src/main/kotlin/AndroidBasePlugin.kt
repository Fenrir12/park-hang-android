import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidBasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("parkhang.serialization")
                apply("parkhang.composeCompiler")
            }

            extensions.configure<LibraryExtension> {
                val versionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

                configureKotlinAndroid(
                    commonExtension = extensions.getByType<LibraryExtension>(),
                    kotlinExtension = extensions.getByType<KotlinAndroidProjectExtension>(),
                    versionCatalog = versionCatalog
                )
            }
        }
    }
}
