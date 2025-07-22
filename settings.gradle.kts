pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        flatDir {
            dirs("${rootProject.projectDir}/libs")
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "ParkHang"

include(":app")

include(":core:designsystem")

include(":feature:parks")
include(":feature:parks")
include(":feature:parks:view")

include(":framework:network:client")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS") // This enables `projects.core.designsystem`
include(":feature:parks:datasource")
include(":feature:parks:entity")
include(":core:common")
include(":feature:parks:di")
