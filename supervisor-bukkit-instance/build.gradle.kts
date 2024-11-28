plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
    id("maven-publish")
}

group = "com.vertmix.supervisor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":supervisor-core"))
    compileOnly(project(":supervisor-instance"))
    compileOnly(project(":supervisor-configuration"))
    compileOnly(project(":supervisor-configuration-yml"))


    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}
