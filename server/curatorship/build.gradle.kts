val kotlinx_serialization_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val mockk_version: String by project
val ktor_version : String by project
val typesafe_config_version: String by project
val coroutines_version: String by project
val valiktor_version: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
    `java-library`
}

group = "br.usp.inovacao.hubusp.curatorship"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":mailer"))

    implementation("org.valiktor:valiktor-core:$valiktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinx_serialization_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("com.typesafe:config:$typesafe_config_version")
    implementation("it.skrape:skrapeit:1.3.0-alpha.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk:$mockk_version")
}
