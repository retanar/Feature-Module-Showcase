plugins {
    id(libs.plugins.convention.android.library.get().pluginId)
    id(libs.plugins.hilt.android.get().pluginId)
    id(libs.plugins.ksp.get().pluginId)
}

android {
    namespace = "com.featuremodule.data"

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_URL", "\"https://randomfox.ca/\"")
        }
    }
}

dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.bundles.network)
    ksp(libs.moshi.kotlin.codegen)
}
