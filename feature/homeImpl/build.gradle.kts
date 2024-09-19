plugins {
    id(libs.plugins.convention.feature.module.get().pluginId)
    id(libs.plugins.google.services.get().pluginId)
}

android {
    namespace = "com.featuremodule.homeImpl"
}

dependencies {
    implementation(projects.feature.homeApi)
    implementation(projects.feature.featureAApi)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
}
