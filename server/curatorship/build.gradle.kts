plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
    `java-library`
}

group = "br.usp.inovacao.hubusp.curatorship"

version = "0.0.1"

repositories { mavenCentral() }

dependencies {
    implementation(project(":config"))
    implementation(project(":mailer"))

    implementation(libs.valiktor.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.skrapeit)
    implementation(libs.bundles.ktor.client)
    implementation(libs.bundles.google.sheets)
    implementation(libs.logback)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.bundles.ktor.client.test)
}
