plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("dev.icerock.mobile.multiplatform")
    id("kotlin-android-extensions")
    id("dev.icerock.mobile.multiplatform.ios-framework")
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

kotlin {
//    targets.getByName<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>("iosX64").binaries.forEach {
//        it.linkerOpts.add("-lsqlite3")
//    }

    targets.filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().forEach{
        it.binaries.filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>()
            .forEach { lib ->
                //println("DBG : ${lib.baseName}")
                //lib.isStatic = false
                lib.linkerOpts.add("-lsqlite3")
            }
    }
}

val mppLibs = listOf(
    Deps.Moko.mvvm,
    Deps.Moko.parcelize
)
val mppModules = listOf(
    Modules.model,
    Modules.database,
    Modules.planetsList,
    Modules.planetDetails
)

framework {
    mppModules.forEach { export(it) }

    export(
        arm64Dependency = "com.squareup.sqldelight:native-driver:${Deps.Versions.sqldelight}",
        x64Dependency = "com.squareup.sqldelight:native-driver:${Deps.Versions.sqldelight}"
    )
    mppLibs.forEach { export(it) }
}

dependencies {
    commonMainApi(Deps.Ktor.client)
    commonMainApi(Deps.Kotlin.kotlinSerialization)
    commonMainApi(Deps.Kotlin.kotlinCoroutines) {
        isForce = true
    }
    //commonMainApi(Deps.Ktor.clientSerialization)
    commonMainImplementation("com.squareup.sqldelight:runtime:${Deps.Versions.sqldelight}")

    //"iosMainApi"("com.squareup.sqldelight:native-driver:${Deps.Versions.sqldelight}")
    //"iosX64MainImplementation"("com.squareup.sqldelight:native-driver:${Deps.Versions.sqldelight}")
    //"iosArm64MainImplementation"("com.squareup.sqldelight:native-driver:${Deps.Versions.sqldelight}")

    iosMainImplementation("com.squareup.sqldelight:native-driver:${Deps.Versions.sqldelight}")

    commonMainImplementation("io.ktor:ktor-client-serialization:${Deps.Ktor.ktor}")
    commonMainImplementation("io.ktor:ktor-client-logging:${Deps.Ktor.ktor}")
    androidMainImplementation("io.ktor:ktor-client-android:${Deps.Ktor.ktor}")
    iosMainImplementation("io.ktor:ktor-client-ios:${Deps.Ktor.ktor}")
    //androidMainImplementation("io.ktor:ktor-client-serialization-jvm:${Deps.Ktor.ktor}")
    //iosMainImplementation("io.ktor:ktor-client-serialization-native:${Deps.Ktor.ktor}")

    mppModule(Modules.model)
    mppModule(Modules.database)
    mppModule(Modules.planetsList)
    mppModule(Modules.planetDetails)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.freeCompilerArgs += listOf("-Xallow-jvm-ir-dependencies")
}
