import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    id("com.utopia-rise.godot-kotlin-jvm") version "0.6.0-3.5.2"
}

group = "dev.gluton"
version = "0.0.1"

repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    languageVersion = "1.8"
}

dependencies {
    implementation(kotlin("reflect"))
}