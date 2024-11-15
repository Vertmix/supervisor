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

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.test {
        useJUnitPlatform()
    }
}

tasks.shadowJar {
    relocate("com.vertmix.supervisor.repository", "com.vertmix.supervisor.core.bukkit.repository")
    relocate("com.vertmix.supervisor.configuration", "com.vertmix.supervisor.core.bukkit.configuration")
}

dependencies {
    implementation(project(":supervisor-core"))
    implementation(project(":supervisor-loader"))
    implementation(project(":supervisor-repository"))
    implementation(project(":supervisor-repository-json"))
    implementation(project(":supervisor-configuration"))
    implementation(project(":supervisor-configuration-yml"))
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}
