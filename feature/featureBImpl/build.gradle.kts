plugins {
    id(libs.plugins.convention.feature.module.get().pluginId)
}

android {
    namespace = "com.featuremodule.featureBImpl"
}

dependencies {
    implementation(projects.feature.featureBApi)
}
