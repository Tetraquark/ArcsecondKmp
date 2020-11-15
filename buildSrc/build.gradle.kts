plugins {
    id("org.jetbrains.kotlin.jvm") version("1.4.10")
}

repositories {
    jcenter()
    google()

    maven { url = uri("https://dl.bintray.com/icerockdev/plugins") }
}

dependencies {
    implementation("dev.icerock:mobile-multiplatform:0.8.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
    implementation("com.android.tools.build:gradle:4.2.0-alpha15")
}
