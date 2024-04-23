pluginManagement {
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
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "feature-module-template"
include(
    ":app",
    ":feature:homeApi",
    ":feature:homeImpl",
    ":feature:featureAApi",
    ":feature:featureAImpl",
    ":feature:foxFeatureApi",
    ":feature:foxFeatureImpl",
    ":core",
    ":data",
)
