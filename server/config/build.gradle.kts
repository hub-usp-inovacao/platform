val kotlin_version: String by project
val typesafe_config_version: String by project

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    `java-library`
}

group = "br.usp.inovacao.hubusp.config"

version = "0.0.1"

repositories { mavenCentral() }

dependencies {
    implementation("com.typesafe:config:$typesafe_config_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
