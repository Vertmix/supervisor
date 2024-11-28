plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
    id("maven-publish")
}

group = "com.vertmix.supervisor"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":supervisor-core"))
    implementation(project(":supervisor-loader"))
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}