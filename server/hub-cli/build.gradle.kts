plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
    alias(libs.plugins.shadow)
    id("org.jetbrains.dokka")
}

application { mainClass.set("br.usp.inovacao.hubusp.server.cli.MainKt") }

tasks {
    shadowJar {
        manifest { attributes(Pair("Main-Class", "br.usp.inovacao.hubusp.server.cli.MainKt")) }
    }
}

repositories { mavenCentral() }

dependencies {
    implementation(project(":config"))
    implementation(project(":curatorship"))
    implementation(project(":hub-app"))
    implementation(project(":techtransfer"))
    implementation(project(":persistence"))

    implementation(libs.kmongo.serialization)
    implementation(libs.clikt)

    implementation(libs.logback)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
