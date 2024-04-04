plugins {
    `convention-android-library`
}

android {
    namespace = "com.featuremodule.home_impl"
}

dependencies {
    implementation(projects.feature.homeApi)
    implementation(projects.feature.featureAApi)
    implementation(projects.core)

    implementation(libs.navigation.compose)
}