plugins {
    id(libs.plugins.convention.feature.module.get().pluginId)
}

android {
    namespace = "com.featuremodule.foxFeatureImpl"
}

dependencies {
    implementation(projects.feature.foxFeatureApi)
    implementation(projects.data)

    implementation(libs.collections.immutable)
    implementation(libs.glide.compose)
}
