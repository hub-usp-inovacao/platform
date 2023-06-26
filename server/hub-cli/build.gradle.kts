val kotlin_version: String by project
val logback_version: String by project
val mockk_version: String by project
val clikt_version: String by project
val kmongo_version: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("org.jetbrains.dokka")
}

tasks {
    shadowJar {
        manifest {
            attributes(
                Pair("Main-Class", "br.usp.inovacao.hubusp.server.cli.MainKt")
            )
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":configuration"))
    implementation(project(":curatorship"))
    implementation(project(":techtransfer"))
    implementation(project(":persistence"))

    implementation("org.litote.kmongo:kmongo-serialization:$kmongo_version")
    implementation("com.github.ajalt.clikt:clikt:$clikt_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk:$mockk_version")
}
