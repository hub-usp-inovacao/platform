val kotlinx_serialization_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val mockk_version: String by project
val kmongo_version: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
    `java-library`
}

group = "br.usp.inovacao.hubusp.persistence"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":catalog"))
    implementation(project(":curatorship"))

    implementation("org.litote.kmongo:kmongo-serialization:$kmongo_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinx_serialization_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk:$mockk_version")
}
