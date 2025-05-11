plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")

}

android {
    namespace = "local.pmdm.cocinaconcatarinaapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "local.pmdm.cocinaconcatarinaapp"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)// Componentes de Material Design
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)// Fragment Navigation Component
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // ViewModel para la arquitectura MVVM
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.fragment) // Corrutinas para el ciclo de vida
    implementation(libs.androidx.room.runtime) //Para usar Room para la persistencia de datos
    kapt(libs.androidx.room.compiler) //Room
    implementation(libs.androidx.room.ktx) //Room
    implementation(libs.material.v1130alpha01) //Material Components (search bar)
    implementation (libs.material.v1xy) //Materias Components para elementos del CardView
    implementation(libs.gson) //Json
    implementation(libs.androidx.media3.ui)//La biblioteca original de ExoPlayer esta obsoleta asi que lo usamos mediante la API AndroidX Media3
    implementation(libs.androidx.media3.exoplayer.hls)
    implementation(libs.androidx.media3.common)



    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}