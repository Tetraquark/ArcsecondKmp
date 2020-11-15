plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("dev.icerock.mobile.multiplatform")
}

configurations {
    create("composeCompiler") {
        isCanBeConsumed = false
    }
}

android {
    compileSdkVersion(Deps.Versions.compileSdkVersion)
    buildToolsVersion(Deps.Versions.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Deps.Versions.minSdkVersion)
        targetSdkVersion(Deps.Versions.targetSdkVersion)
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

    // Compose-MPP workaround: https://github.com/avdim/compose_mpp_workaround

    afterEvaluate {
        val composeCompilerJar =
            configurations["composeCompiler"]
                .resolve()
                .singleOrNull()
                ?: error("Please add \"androidx.compose:compose-compiler\" (and only that) as a \"composeCompiler\" dependency")
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions.freeCompilerArgs += listOf("-Xuse-ir", "-Xplugin=$composeCompilerJar")
        }
    }

}

dependencies {
    mppModule(Modules.model)

    commonMainApi(Deps.Kotlin.kotlinCoroutines)
    commonMainApi(Deps.Moko.mvvm.common)
    commonMainImplementation(Deps.oolong)

    androidMainImplementation(Deps.Android.lifecycleExt)
    androidMainImplementation(Deps.Android.coreKtx)
    androidMainImplementation(Deps.Android.appcompat)
    androidMainImplementation(Deps.Android.fragmentKtx)
    androidMainImplementation(Deps.Android.material)

    androidMainImplementation(Deps.Compose.ui)
    androidMainImplementation(Deps.Compose.uiTooling)
    androidMainImplementation(Deps.Compose.material)
    androidMainImplementation(Deps.Compose.runtime)
    androidMainImplementation(Deps.Compose.runtimeLivedata)
    "composeCompiler"("androidx.compose.compiler:compiler:${Deps.Compose.composeVersion}")
}
