plugins {
    `convention-android-library`
}

android {
    namespace = "com.featuremodule.home_impl"
}

dependencies {
    implementation(projects.feature.homeApi)

    implementation(libs.navigation.compose)
}