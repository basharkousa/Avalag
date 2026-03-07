import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.bashar.avalag"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.bashar.avalag"
        minSdk = 24
//        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

//        buildConfigField("String", "BASE_URL", "\"https://adverwize.smarttarget.qa/api/v1/\"")


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Room schema export (handy for migrations)
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.incremental" to "true")
            }
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("SIGNING_STORE_FILE") ?: "keystore.jks")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }


    flavorDimensions += "env"

    productFlavors {
        create("dev") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"

            // Optional but very useful for debugging installs:
            resValue("string", "app_name", "Avalag Dev")

            buildConfigField("String", "BASE_URL", "\"https://adverwize.smarttarget.qa/api/v1/\"")
        }

        create("prod") {
            dimension = "env"
            // keep original app id + name
            buildConfigField("String", "BASE_URL", "\"https://adverwize.smarttarget.qa/api/v1/\"")
        }
    }

    buildTypes {

        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }

        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }


    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs.addAll(
                "-Xjvm-default=all",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
            )
        }
    }


    buildFeatures {
        compose = true
        buildConfig = true
    }
    // Using Kotlin Compose plugin, no need to set composeCompilerVersion manually
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
        animationsDisabled = true
    }

}

dependencies {

// ----- Compose (via BOM) -----
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.compose.material.icons.extended)


    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // DI: Hilt (KSP)
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)


    // Data Store
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.appcompat)              // setApplicationLocales


    // Data: Room (KSP)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    // For paging support
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)

    // Network
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.converter.gson) // or swap to kotlinx-serialization converter

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Images
    implementation(libs.coil.compose)

    // multidex (for large app sizes 64k...)
    implementation (libs.androidx.multidex)

    implementation(libs.libphonenumber)

//     Misc
    implementation(libs.timber)

    // ---- Tests ----
    testImplementation(libs.junit)

    //For ViewModel
    testImplementation(libs.kotlinx.coroutines.test)


    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Compose UI tests (no version thanks to BOM)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Debug test tooling
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

// Room schema location for KSP (keeps JSON schemas under projectDir/schemas)
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.generateKotlin", "true")
}

// Allow references to generated code (Hilt, Room) in unit tests if needed
configurations.all {
    resolutionStrategy {
        // keep aligned if you add extra libs later
    }
}