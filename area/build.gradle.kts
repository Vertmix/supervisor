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
    maven("https://maven.enginehub.org/repo/") // WorldEdit repository
    maven("https://mvn.lumine.io/repository/maven-public/") // ModelEngine repository
    maven("https://repo.aikar.co/content/groups/aikar/")

}


tasks.shadowJar {
    relocate("com.vertmix.supervisor", "com.vertmix.tycoon.core")

}

dependencies {
    implementation(project(":supervisor-core"))
    implementation(project(":supervisor-bukkit"))
    implementation(project(":supervisor-loader"))
    implementation(project(":supervisor-repository"))
    implementation(project(":supervisor-repository-json"))
    implementation(project(":supervisor-configuration"))
    implementation(project(":supervisor-configuration-yml"))
    implementation(project(":supervisor-bukkit-item"))
    implementation(project(":supervisor-adapter"))
    implementation(project(":supervisor-adapter-bukkit"))
    implementation(project(":supervisor-module-loader"))

    implementation(project(":supervisor-bukkit-menu"))

    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.14") // Latest WorldEdit version for Bukkit/Paper
    compileOnly("com.ticxo.modelengine:ModelEngine:R4.0.4")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")

}
