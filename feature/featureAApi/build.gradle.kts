plugins {
    id(libs.plugins.convention.android.library.get().pluginId)
}

android {
    namespace = "com.featuremodule.featureAApi"
}

dependencies {
    implementation(libs.navigation.compose)
}
