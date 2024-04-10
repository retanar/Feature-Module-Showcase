plugins {
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
}

detekt {
    config.setFrom(rootProject.file("detekt-config.yml"))
    basePath = projectDir.absolutePath
}

ktlint {
    android = true
    outputColorName = "RED"
}
