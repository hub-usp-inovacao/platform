plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    `java-library`
}

group = "br.usp.inovacao.hubusp.config"

version = "0.0.1"

repositories { mavenCentral() }

dependencies {
    implementation(libs.bundles.hoplite)
    testImplementation(libs.junit)
}
