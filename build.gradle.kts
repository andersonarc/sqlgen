@file:Suppress("SpellCheckingInspection")

plugins {
    java
    kotlin("jvm") version "1.5.0"
}

group = "com.github.andersonarc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.xerial", "sqlite-jdbc", "3.7.2")
    testImplementation("junit", "junit", "4.12")
}