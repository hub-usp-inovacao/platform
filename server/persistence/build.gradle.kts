plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
    `java-library`
}

group = "br.usp.inovacao.hubusp.persistence"

version = "0.0.1"

repositories { mavenCentral() }

dependencies {
    implementation(project(":catalog"))
    implementation(project(":config"))
    implementation(project(":curatorship"))
    implementation(project(":discovery"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.ktor.client)
    implementation(libs.kmongo.serialization)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.logback)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
