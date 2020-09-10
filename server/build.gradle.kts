plugins {
    kotlin("jvm")
    application
}

application {
    mainClassName = "io.ktor.server.jetty.DevelopmentEngine"
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/kotlin")
sourceSets["main"].resources.srcDirs("resources")

dependencies {
    //implementation("org.jetbrains.kotlin:kotlin-stdlib:${Deps.Kotlin.kotlin}")
    implementation(Deps.Ktor.serverJetty)
    implementation(Deps.Ktor.htmlBuilder)
    implementation(Deps.Ktor.clientCio)
    implementation(Deps.Kotlin.kotlinSerialization)

    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("com.squareup.sqldelight:sqlite-driver:${Deps.Versions.sqldelight}")

    implementation(project(":model"))
    implementation(project(":database"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
