plugins {
    id(libs.plugins.convention.feature.module.get().pluginId)
}

android {
    namespace = "com.featuremodule.home_impl"
}

dependencies {
    implementation(projects.feature.homeApi)
    implementation(projects.feature.featureAApi)
    implementation(projects.core)
}