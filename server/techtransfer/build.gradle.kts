plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
    `java-library`
}

group = "br.usp.inovacao.hubusp.techtransfer"

version = "0.0.1"

repositories { mavenCentral() }

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.logback)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
