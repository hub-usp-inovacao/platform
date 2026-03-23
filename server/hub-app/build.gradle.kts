plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
    alias(libs.plugins.shadow)
    id("org.jetbrains.dokka")
}

group = "br.usp.inovacao.hubusp"

version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks {
    shadowJar { manifest { attributes(Pair("Main-Class", "br.usp.inovacao.hubusp.server.app")) } }
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation(project(":catalog"))
    implementation(project(":config"))
    implementation(project(":curatorship"))
    implementation(project(":discovery"))
    implementation(project(":techtransfer"))
    implementation(project(":persistence"))
    implementation(project(":mailer"))
    implementation(project(":sheets"))

    implementation(libs.kmongo.serialization)

    // renata: projeto precisa do OAuth2 Client para implementar autenticacao USP
    implementation("io.ktor:ktor-server-auth-jvm") // Para o servidor entender OAuth
    implementation("io.ktor:ktor-client-apache-jvm") // Para o HttpClient(Apache) funcionar

    implementation(libs.bundles.ktor.server)
    testImplementation(libs.bundles.ktor.client)
    testImplementation(libs.bundles.ktor.server.test)

    implementation(libs.tika.core)
    implementation(libs.html.sanitizer)

    implementation(libs.logback)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
