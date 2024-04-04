plugins {
    `convention-android-library`
}

android {
    namespace = "com.featuremodule.feature_a_impl"
}

dependencies {
    implementation(projects.feature.featureAApi)
}