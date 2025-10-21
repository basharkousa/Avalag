// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // App & Compose
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose) apply false

    // Tooling used in modules (declare here, apply in modules)
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

// Optional: a simple clean task (Gradle 8+ doesn't add it by default)
tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
