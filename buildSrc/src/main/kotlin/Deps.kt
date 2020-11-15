object Deps {
    object Versions {
        const val compileSdkVersion = 29
        const val minSdkVersion = 23
        const val targetSdkVersion = 29
        const val buildToolsVersion = "29.0.3"

        const val serialization = "1.0.0-RC2"
        const val sqldelight = "1.4.3"
    }

    const val oolong = "org.oolong-kt:oolong:2.1.0"

    object Kotlin {
        const val kotlin = "1.4.10"

        const val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0-RC2"
        const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9-native-mt-2"
    }

    object Ktor {
        const val ktor = "1.4.1"

        const val serverJetty = "io.ktor:ktor-server-jetty:$ktor"
        const val htmlBuilder = "io.ktor:ktor-html-builder:$ktor"
        const val client = "io.ktor:ktor-client-core:$ktor"
        const val clientCIO = "io.ktor:ktor-client-cio:$ktor"
        const val clientSerialization = "io.ktor:ktor-client-serialization:$ktor"
    }

    object Moko {
        val mvvm = MultiPlatformLibrary(
            common = "dev.icerock.moko:mvvm:0.8.0",
            iosX64 = "dev.icerock.moko:mvvm-iosx64:0.8.0",
            iosArm64 = "dev.icerock.moko:mvvm-iosarm64:0.8.0",
        )
        val parcelize = MultiPlatformLibrary(
            common = "dev.icerock.moko:parcelize:0.4.0",
            iosX64 = "dev.icerock.moko:parcelize-iosx64:0.4.0",
            iosArm64 = "dev.icerock.moko:parcelize-iosarm64:0.4.0",
        )
    }

    object Android {
        const val coreKtx = "androidx.core:core-ktx:1.3.1"
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.1"
        const val material = "com.google.android.material:material:1.3.0-alpha02"
        const val lifecycleExt = "androidx.lifecycle:lifecycle-extensions:2.2.0"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.2.5"
    }

    object Compose {
        const val composeVersion = "1.0.0-alpha07"

        const val ui = "androidx.compose.ui:ui:$composeVersion"
        const val uiTooling = "androidx.ui:ui-tooling:$composeVersion"
        const val foundation = "androidx.compose.foundation:foundation:$composeVersion"
        const val material = "androidx.compose.material:material:$composeVersion"
        const val runtime = "androidx.compose.runtime:runtime:$composeVersion"
        const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata:$composeVersion"
    }
}
