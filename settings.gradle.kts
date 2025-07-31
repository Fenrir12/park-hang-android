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
include(":core:event")
include(":core:designsystem")
include(":core:designsystem:components")

include(":core:userprofile")

include(":core:checkin")

include(":feature:parks:datasource")
include(":feature:parks:entity")
include(":feature:parks:di")
include(":feature:parks:view")

include(":feature:profile:view")
include(":feature:profile:entity")
include(":feature:profile:di")
include(":feature:profile:datasource")

include(":feature:hangout:view")
include(":feature:hangout:entity")
include(":feature:hangout:di")
include(":feature:hangout:datasource")

include(":framework:network:client")
include(":framework:authentication")
include(":framework:geolocation")

include(":framework:persistence:datasource")
include(":framework:persistence:localdatastorage")
include(":framework:persistence:datasource:usercredentialspreferences")
include(":framework:persistence:datasource:checkinpreferences")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

