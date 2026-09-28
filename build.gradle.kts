  plugins {
    kotlin("jvm") version "2.3.20"
}

group = "cl.duoc.taskflow"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}