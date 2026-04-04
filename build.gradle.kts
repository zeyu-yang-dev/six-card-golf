plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "io.github.zeyuyangdev.sixcardgolf"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(group = "tools.aqua", name = "bgw-gui", version = "0.9")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("io.github.zeyuyangdev.sixcardgolf.MainKt")

    applicationDefaultJvmArgs = listOf(
        "--add-opens", "java.desktop/sun.awt=ALL-UNNAMED",
        "--add-opens", "java.desktop/java.awt.peer=ALL-UNNAMED",
        "--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED",
        "--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED"
    )
}