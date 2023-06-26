val kotlin_version: String by project
val logback_version: String by project
val typesafe_config_version: String by project

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    `java-library`
}

group = "br.usp.inovacao.hubusp.configuration"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.typesafe:config:$typesafe_config_version")
}
