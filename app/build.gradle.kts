plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.projectcubes42"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.projectcubes42"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Dépendances principales de l'application
    implementation(libs.squareup.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.fragment.ktx)
    implementation(libs.lifecycle.viewmodel.ktx.v261)
    implementation(libs.material)
    implementation(libs.appcompat)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.annotation)
    implementation(libs.cronet.embedded)
    implementation(libs.biometric)
    implementation(libs.spring.security.crypto)

    // Dépendances de test
    testImplementation(libs.junit) // JUnit 4
    testImplementation(libs.mockito.core.v531) // Mockito core

    // LiveData Testing
    testImplementation(libs.core.testing.v220) // androidx.arch.core:core-testing:2.2.0

    // Dépendances de test d'instrumentation
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // (Optionnel) Si tu utilises les règles Espresso comme ActivityTestRule
    androidTestImplementation (libs.rules)

    // (Optionnel) Le runner d'AndroidX, si tu en as besoin
    androidTestImplementation (libs.runner)

    testImplementation (libs.mockwebserver)
}