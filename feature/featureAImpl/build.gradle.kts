plugins {
    id(libs.plugins.convention.feature.module.get().pluginId)
}

android {
    namespace = "com.featuremodule.featureAImpl"
}

dependencies {
    implementation(projects.feature.featureAApi)
}
