plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.dokka)
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    allprojects { dokka(project) }

    kover(project(":catalog"))
    kover(project(":config"))
    kover(project(":curatorship"))
    kover(project(":discovery"))
    kover(project(":hub-app"))
    kover(project(":hub-cli"))
    kover(project(":mailer"))
    kover(project(":persistence"))
    kover(project(":sheets"))
    kover(project(":techtransfer"))
}