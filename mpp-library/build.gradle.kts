plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("dev.icerock.mobile.multiplatform")
    id("kotlin-android-extensions")
}

group = "ru.tetraquark.arcsecondkmp"
version = "1.0.0"

android {
    compileSdkVersion(Deps.Versions.compileSdkVersion)
    defaultConfig {
        minSdkVersion(Deps.Versions.minSdkVersion)
        targetSdkVersion(Deps.Versions.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    commonMainApi(Deps.Ktor.clientCio)
    commonMainApi(Deps.Kotlin.kotlinCoroutines)
    commonMainApi(Deps.Kotlin.kotlinSerialization)
    commonMainApi(Deps.Ktor.clientSerialization)
    commonMainApi("com.squareup.sqldelight:runtime:${Deps.Versions.sqldelight}")

    mppModule(MultiPlatformModule(
        name = ":model",
        exported = true
    ))
    mppModule(MultiPlatformModule(
        name = ":database",
        exported = true
    ))
    mppModule(MultiPlatformModule(
        name = ":mpp-library:feature:planets-list",
        exported = true
    ))
    mppModule(MultiPlatformModule(
        name = ":mpp-library:feature:planet-details",
        exported = true
    ))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.freeCompilerArgs += listOf("-Xallow-jvm-ir-dependencies")
}
