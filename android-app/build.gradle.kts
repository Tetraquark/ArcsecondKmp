plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(Deps.Versions.compileSdkVersion)
    buildToolsVersion(Deps.Versions.buildToolsVersion)

    defaultConfig {
        applicationId = "ru.tetraquark.arcsecondkmp"
        minSdkVersion(Deps.Versions.minSdkVersion)
        targetSdkVersion(Deps.Versions.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }

    composeOptions {
        kotlinCompilerVersion = Deps.Kotlin.kotlin
        kotlinCompilerExtensionVersion = Deps.Compose.composeVersion
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

dependencies {
    implementation(project(":mpp-library"))
    implementation(project(":mpp-library:feature:planets-list"))
    implementation(project(":mpp-library:feature:planet-details"))

    implementation(Deps.Android.coreKtx)
    implementation(Deps.Android.appcompat)
    implementation(Deps.Android.constraintlayout)
    implementation(Deps.Android.material)

    implementation(Deps.Compose.ui)
    implementation(Deps.Compose.uiTooling)
    implementation(Deps.Compose.material)
    implementation(Deps.Compose.runtime)
    implementation(Deps.Compose.runtimeLivedata)

    implementation(Deps.Moko.mvvm.common)
    implementation("com.squareup.sqldelight:android-driver:${Deps.Versions.sqldelight}")
}
