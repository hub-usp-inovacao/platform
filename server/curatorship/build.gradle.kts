val kotlinx_serialization_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val mockk_version: String by project
val ktor_version : String by project
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
    implementation(project(":config"))
    implementation(project(":mailer"))

    implementation("org.valiktor:valiktor-core:$valiktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinx_serialization_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("it.skrape:skrapeit:1.3.0-alpha.1")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.37.1")
    implementation("com.google.api-client:google-api-client:2.7.2")
    implementation("com.google.apis:google-api-services-sheets:v4-rev20250211-2.0.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk:$mockk_version")
}
