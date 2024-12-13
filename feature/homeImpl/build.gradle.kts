plugins {
    id(libs.plugins.convention.feature.module.get().pluginId)
}

android {
    namespace = "com.featuremodule.homeImpl"
}

dependencies {
    implementation(projects.feature.homeApi)
    implementation(projects.feature.featureAApi)

    implementation(libs.bundles.exoplayer)
    implementation(libs.bundles.camerax)
    implementation(libs.camerax.mlkit)
    implementation(libs.gms.mlkit.barcode)

    implementation(libs.glide.compose)
}
