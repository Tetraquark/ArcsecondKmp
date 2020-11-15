buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.0-alpha15")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Deps.Kotlin.kotlin}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Deps.Kotlin.kotlin}")
        classpath("com.squareup.sqldelight:gradle-plugin:${Deps.Versions.sqldelight}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()

        maven { url = uri("https://kotlin.bintray.com/kotlin") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        maven { url = uri("https://kotlin.bintray.com/ktor") }
        maven { url = uri("https://dl.bintray.com/icerockdev/moko") }
        maven { url = uri("https://dl.bintray.com/arkivanov/maven") }
    }
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}
