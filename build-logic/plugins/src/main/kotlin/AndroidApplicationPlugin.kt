import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("jacoco")
            }

            extensions.configure<BaseAppModuleExtension> {
                val versionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

                configureKotlinAndroid(this, versionCatalog)
                defaultConfig.targetSdk = versionCatalog.findVersion("targetSdk").get().toString().toInt()
            }
        }
    }
}
