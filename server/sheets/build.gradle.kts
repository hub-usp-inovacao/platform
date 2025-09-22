val kotlin_version: String by project
val mockk_version: String by project

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    `java-library`
}

group = "br.usp.inovacao.hubusp.sheets"

version = "0.0.1"

repositories { mavenCentral() }

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk:$mockk_version")

    implementation("com.google.auth:google-auth-library-oauth2-http:1.37.1")
    implementation("com.google.api-client:google-api-client:2.7.2")
    api("com.google.apis:google-api-services-sheets:v4-rev20250211-2.0.0")
}
