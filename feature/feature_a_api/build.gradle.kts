plugins {
    id(libs.plugins.convention.android.library.get().pluginId)
}

android {
    namespace = "com.featuremodule.feature_a_api"
}

dependencies {
    implementation(libs.navigation.compose)
}