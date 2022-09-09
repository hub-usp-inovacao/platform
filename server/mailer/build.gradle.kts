val kotlin_version: String by project
val logback_version: String by project
val mockk_version: String by project
val javax_mail_version: String by project

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    `java-library`
}

group = "br.usp.inovacao.hubusp.mailer"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("javax.mail:mail:$javax_mail_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk:$mockk_version")
}
