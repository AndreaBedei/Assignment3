/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.6/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    maven {
        url = uri("https://repo.akka.io/maven")
    }
}

dependencies {
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Assginment 3 - Akka
    implementation(platform("com.typesafe.akka:akka-bom_2.13:2.9.3"))

    implementation("com.typesafe.akka:akka-persistence-typed_2.13")
    testImplementation("com.typesafe.akka:akka-persistence-testkit_2.13")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "org.example.App"
}
