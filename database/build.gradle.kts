plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("com.squareup.sqldelight")
}

android {
    compileSdkVersion(Deps.Versions.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Deps.Versions.minSdkVersion)
        targetSdkVersion(Deps.Versions.targetSdkVersion)
    }

    sourceSets.forEach {
        it.manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

kotlin {
    targets {
        android()
        jvm()
        ios()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":model"))
                implementation("com.squareup.sqldelight:runtime:${Deps.Versions.sqldelight}")
            }
        }
        val androidMain by getting
        val jvmMain by getting
        val iosMain by getting
    }
}

sqldelight {
    database("ArcsecondDatabase") {
        packageName = "ru.tetraquark.arcsecondkmp.database"
    }
}
