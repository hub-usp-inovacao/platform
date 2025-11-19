plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    `java-library`
}

group = "br.usp.inovacao.hubusp.sheets"

version = "0.0.1"

repositories { mavenCentral() }

dependencies {
    implementation(libs.bundles.google.sheets)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
