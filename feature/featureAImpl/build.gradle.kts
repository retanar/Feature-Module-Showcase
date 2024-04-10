plugins {
    id(libs.plugins.convention.feature.module.get().pluginId)
}

android {
    namespace = "com.featuremodule.feature_a_impl"
}

dependencies {
    implementation(projects.feature.featureAApi)
    implementation(projects.core)
}