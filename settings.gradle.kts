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

include(":core:common")
include(":core:designsystem")
include(":core:designsystem:components")

include(":core:userprofile")

include(":feature:parks")
include(":feature:parks")
include(":feature:parks:view")

include(":feature:parks:datasource")
include(":feature:parks:entity")
include(":feature:parks:di")

include(":feature:profile:view")
include(":feature:profile:entity")
include(":feature:profile:di")
include(":feature:profile:datasource")

include(":framework:network:client")
include(":framework:authentication")
include(":framework:geolocation")

include(":framework:persistence:datasource")
include(":framework:persistence:localdatastorage")
include(":framework:persistence:datasource:usercredentialspreferences")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS") // This enables `projects.core.designsystem`

