plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    targets {
        jvm()
        ios()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(Deps.Kotlin.kotlinSerialization)
            }
        }
        val jvmMain by getting
        val iosMain by getting
    }
}
