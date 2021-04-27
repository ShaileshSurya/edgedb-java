/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java Library project to get you started.
 * For more details take a look at the Java Libraries chapter in the Gradle
 * User Manual available at https://docs.gradle.org/6.2.2/userguide/java_library_plugin.html
 */

plugins {
    `java-library`
    id("io.freefair.lombok") version "5.2.1"
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("com.fasterxml.jackson.core:jackson-core:2.11.3")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.11.3")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.3")
 //   implementation("com.google.guava:guava:30.1.1-jre")

    testImplementation("org.mockito:mockito-core:3.5.15")
    testImplementation("junit:junit:4.12")
}
