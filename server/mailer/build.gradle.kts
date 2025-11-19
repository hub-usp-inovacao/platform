plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    `java-library`
}

group = "br.usp.inovacao.hubusp.mailer"

version = "0.0.1"

repositories { mavenCentral() }

dependencies {
    implementation(libs.javax.mail)
    implementation(libs.logback)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
