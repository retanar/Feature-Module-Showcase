plugins {
    id(libs.plugins.convention.android.library.get().pluginId)
    id(libs.plugins.hilt.android.get().pluginId)
    id(libs.plugins.ksp.get().pluginId)
}

android {
    namespace = "com.featuremodule.core"
}

dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}